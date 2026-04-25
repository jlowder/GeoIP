package com.example.ipgeolookup

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.ipgeolookup.databinding.ActivityMainBinding
import com.example.ipgeolookup.ui.viewmodel.GeoLocationViewModel
import com.example.ipgeolookup.ui.viewmodel.GeoLocationUiState
import com.example.ipgeolookup.data.model.GeoLocation
import com.example.ipgeolookup.data.model.getFullLocation
import com.example.ipgeolookup.data.model.getFormattedCoordinates
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.net.InetAddress
import java.net.UnknownHostException

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: GeoLocationViewModel
    private var mapInitialized = false
    private lateinit var sharedPreferences: SharedPreferences
    
    // Permission request launcher
    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true ||
                      permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        if (granted) {
            // Permission granted, continue with location-based features
        } else {
            // Permission denied, show a message
            Toast.makeText(
                this,
                R.string.permission_denied,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
        
        // Initialize OSMDroid configuration
        Configuration.getInstance().load(
            this,
            sharedPreferences
        )
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        viewModel = ViewModelProvider(this)[GeoLocationViewModel::class.java]
        
        setupToolbar()
        setupMap()
        setupObservers()
        setupListeners()
        
        // Auto-detect IP on startup
        if (savedInstanceState == null) {
            detectCurrentLocation()
        }
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.app_name)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
    
    private fun setupMap() {
        val mapView = binding.mapView
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)
        mapView.overlays.clear()
        mapInitialized = true
        
        // Center map on default location
        val geoPoint = GeoPoint(48.8566, 2.3522) // Paris
        mapView.controller.setCenter(geoPoint)
        mapView.controller.setZoom(3.0)
    }

    private fun setupListeners() {
        binding.searchButton.setOnClickListener {
            val ipInput = binding.ipInput.text.toString().trim()
            if (ipInput.isNotEmpty()) {
                validateAndLookupIp(ipInput)
            } else {
                Toast.makeText(
                    this,
                    R.string.hint_enter_ip,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.ipInput.setOnEditorActionListener { _, actionId, _ ->
            // Use EditorInfo action constants instead of KeyEvent codes
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH ||
                actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                val ipInput = binding.ipInput.text.toString().trim()
                if (ipInput.isNotEmpty()) {
                    validateAndLookupIp(ipInput)
                }
                true
            } else {
                false
            }
        }

        binding.copyButton.setOnClickListener {
            copyLocationToClipboard()
        }
        
        binding.autoDetectButton.setOnClickListener {
            detectCurrentLocation()
        }
        
        binding.coordinatesMapAction.setOnClickListener {
            openLocationOnMap()
        }
    }
    
    private fun detectCurrentLocation() {
        // Check location permissions
        val hasPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        
        if (hasPermission) {
            // Permission already granted, proceed with auto-detection
            showLoading()
            // For now, use the IP-based detection
            viewModel.getIpInfo()
        } else {
            // Request permission
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
            Toast.makeText(
                this,
                R.string.permission_required,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                // The 'when' expression must handle all cases of GeoLocationUiState
                when (state) {
                    is GeoLocationUiState.Idle -> {
                        // Initial state, do nothing
                    }

                    is GeoLocationUiState.Loading -> {
                        showLoading()
                    }

                    is GeoLocationUiState.Success -> {
                        hideLoading()
                        showLocationDetails(state.location)
                    }

                    is GeoLocationUiState.Error -> {
                        hideLoading()
                        showError(state.message)
                    }
                }
            }
        }
    }
    private fun validateAndLookupIp(ip: String) {
        // Simple IP validation (basic check)
        if (!is_valid_ip(ip)) {
            showError(R.string.error_invalid_ip)
            return
        }

        // Check if input is a hostname (contains dots but not a valid IP)
        val ipv4Pattern = Regex("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$")
        val ipv6Pattern = Regex("^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|::|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(?::[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(?::[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(?::[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(?::[0-9a-fA-F]{1,4}){1,5}|:[0-9a-fA-F]{1,4}(?::[0-9a-fA-F]{1,4}){1,6}|:|::ffff:[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$")
        val isHostname = ip.contains('.') && !ipv4Pattern.matches(ip) && !ipv6Pattern.matches(ip)

        if (isHostname) {
            // Resolve hostname to IP first
            try {
                val hosts = InetAddress.getAllByName(ip)
                if (hosts.isNotEmpty()) {
                    val resolvedIp = hosts[0].hostAddress
                    showLoading()
                    viewModel.getIpInfo(resolvedIp)
                } else {
                    showError(R.string.error_host_not_found)
                }
            } catch (e: UnknownHostException) {
                showError(R.string.error_host_not_found)
            } catch (e: Exception) {
                showError(R.string.error_lookup_failed)
            }
        } else {
            showLoading()
            viewModel.getIpInfo(ip)
        }
    }

    private fun updateMapLocation(latitude: Double?, longitude: Double?) {
        if (!mapInitialized || latitude == null || longitude == null) return

        val geoPoint = GeoPoint(latitude, longitude)
        binding.mapView.controller.animateTo(geoPoint) // Smoother transition
        binding.mapView.controller.setZoom(10.0)

        // FIX: Clear previous markers to prevent overlapping
        binding.mapView.overlays.clear()

        // Add marker
        val marker = Marker(binding.mapView)
        marker.position = geoPoint
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = getString(R.string.location_marker_title) // Use resource if available
        marker.subDescription = binding.cityValue.text.toString()
        binding.mapView.overlays.add(marker)
        binding.mapView.invalidate()
    }

    private fun is_valid_ip(ip: String): Boolean {
        // Support IPv4, IPv6, IPv4-mapped IPv6, hostnames, and case-insensitive IPv6
        val ipv4Pattern = Regex("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$")
        val ipv6Pattern = Regex("^(?:(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|::(?:[0-9a-fA-F]{1,4}:){0,6}[0-9a-fA-F]{1,4}|(?:[0-9a-fA-F]{1,4}:){1,7}:|(?:[0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|(?:[0-9a-fA-F]{1,4}:){1,5}(?::[0-9a-fA-F]{1,4}){1,2}|(?:[0-9a-fA-F]{1,4}:){1,4}(?::[0-9a-fA-F]{1,4}){1,3}|(?:[0-9a-fA-F]{1,4}:){1,3}(?::[0-9a-fA-F]{1,4}){1,4}|(?:[0-9a-fA-F]{1,4}:){1,2}(?::[0-9a-fA-F]{1,4}){1,5}|:[0-9a-fA-F]{1,4}(?::[0-9a-fA-F]{1,4}){1,6}|:|::ffff:)?(?:\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|(?:(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|::(?:[0-9a-fA-F]{1,4}:){0,6}[0-9a-fA-F]{1,4}|(?:[0-9a-fA-F]{1,4}:){1,7}:|(?:[0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|(?:[0-9a-fA-F]{1,4}:){1,5}(?::[0-9a-fA-F]{1,4}){1,2}|(?:[0-9a-fA-F]{1,4}:){1,4}(?::[0-9a-fA-F]{1,4}){1,3}|(?:[0-9a-fA-F]{1,4}:){1,3}(?::[0-9a-fA-F]{1,4}){1,4}|(?:[0-9a-fA-F]{1,4}:){1,2}(?::[0-9a-fA-F]{1,4}){1,5}|:[0-9a-fA-F]{1,4}(?::[0-9a-fA-F]{1,4}){1,6}|:)$)")
        val hostnamePattern = Regex("^[a-zA-Z0-9]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(\\.[a-zA-Z0-9]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")
        return ipv4Pattern.matches(ip) || ipv6Pattern.matches(ip) || hostnamePattern.matches(ip)
    }
    
    private fun showLocationDetails(location: GeoLocation) {
        // Update UI with location data
        binding.ipAddressValue.text = location.ipAddress
        binding.countryValue.text = location.countryCode ?: "N/A"
        binding.regionValue.text = location.region ?: "N/A"
        binding.cityValue.text = location.city ?: "N/A"
        binding.coordinatesValue.text = location.getFormattedCoordinates()
        binding.organizationValue.text = location.organization ?: "N/A"
        binding.ispValue.text = location.isp ?: "N/A"
        
        // Display IP type indicator
        val ipType = if (location.ipAddress.contains(':')) "IPv6" else "IPv4"
        binding.ipTypeValue.text = ipType
        
        // Show content container and hide error message
        binding.contentContainer.visibility = NestedScrollView.VISIBLE
        
        // Update map if coordinates are available
        if (location.latitude != null && location.longitude != null) {
            updateMapLocation(location.latitude, location.longitude)
            binding.mapPlaceholder.visibility = android.view.View.GONE
        } else {
            binding.mapPlaceholder.visibility = android.view.View.VISIBLE
        }
    }

    private fun openLocationOnMap() {
        val coordinates = binding.coordinatesValue.text.toString()
        if (coordinates.contains(",")) {
            try {
                val parts = coordinates.split(",")
                val lat = parts[0].trim().toDouble()
                val lon = parts[1].trim().toDouble()
                
                // Use standard geo URI format: geo:$lat,$lon (direct coordinates)
                val geoUri = Uri.parse("geo:$lat,$lon")
                val mapIntent = Intent(Intent.ACTION_VIEW, geoUri).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                
                var launched = false
                
                // Try to launch with geo intent (allows system to choose any map app)
                if (mapIntent.resolveActivity(packageManager) != null) {
                    try {
                        startActivity(mapIntent)
                        launched = true
                    } catch (e: Exception) {
                        // Fall through to web fallback
                    }
                }
                
                // Web-based fallback to Google Maps
                if (!launched) {
                    val webUri = Uri.parse("http://maps.google.com/?q=$lat,$lon")
                    val webIntent = Intent(Intent.ACTION_VIEW, webUri).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    
                    if (webIntent.resolveActivity(packageManager) != null) {
                        try {
                            startActivity(webIntent)
                            launched = true
                        } catch (e: Exception) {
                            // Fall through to error
                        }
                    }
                }
                
                if (!launched) {
                    Toast.makeText(
                        this,
                        "No mapping app found. Please install Google Maps.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "Invalid coordinates",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                this,
                "Coordinates not available",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    
    private fun copyLocationToClipboard() {
        val ipAddress = binding.ipAddressValue.text.toString()
        val ipType = binding.ipTypeValue.text.toString()
        val location = "IP Address: $ipAddress\nIP Type: $ipType\n" +
                "${binding.countryValue.text}\n" +
                "${binding.regionValue.text}\n" +
                "${binding.cityValue.text}\n" +
                "${binding.coordinatesValue.text}\n" +
                "${binding.organizationValue.text}\n" +
                "${binding.ispValue.text}"
        
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("location_info", location)
        clipboard.setPrimaryClip(clip)
        
        Toast.makeText(
            this,
            R.string.location_copied,
            Toast.LENGTH_SHORT
        ).show()
    }
    
    private fun showLoading() {
        binding.loadingIndicator.visibility = android.view.View.VISIBLE
        binding.contentContainer.visibility = NestedScrollView.GONE
        binding.errorMessage.visibility = android.view.View.GONE
    }
    
    private fun hideLoading() {
        binding.loadingIndicator.visibility = android.view.View.GONE
    }
    
    private fun showError(messageResId: Int) {
        hideLoading()
        binding.errorMessage.text = getString(messageResId)
        binding.errorMessage.visibility = android.view.View.VISIBLE
    }

    private fun showError(message: String) {
        hideLoading()
        binding.errorMessage.text = message
        binding.errorMessage.visibility = android.view.View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        // OSMDroid requirement
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        // OSMDroid requirement
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        // OSMDroid requirement: prevents memory leaks
        binding.mapView.onDetach()
    }
} // End of MainActivity class