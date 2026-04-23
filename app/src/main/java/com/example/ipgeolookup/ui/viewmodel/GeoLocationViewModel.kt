package com.example.ipgeolookup.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ipgeolookup.data.model.GeoLocation
import com.example.ipgeolookup.data.model.Result
import com.example.ipgeolookup.data.repository.GeoLocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GeoLocationViewModel : ViewModel() {
    
    private val repository = GeoLocationRepository()
    
    private val _uiState = MutableStateFlow<GeoLocationUiState>(GeoLocationUiState.Idle)
    val uiState: StateFlow<GeoLocationUiState> = _uiState
    
    fun getIpInfo(ip: String? = null) {
        _uiState.value = GeoLocationUiState.Loading
        
        viewModelScope.launch {
            val result = repository.getIpInfo(ip)
            when (result) {
                is Result.Success -> {
                    _uiState.value = GeoLocationUiState.Success(result.data as GeoLocation)
                }
                is Result.Error -> {
                    _uiState.value = GeoLocationUiState.Error(result.message)
                }
                Result.Loading -> {
                    _uiState.value = GeoLocationUiState.Loading
                }
            }
        }
    }
}

sealed class GeoLocationUiState {
    object Idle : GeoLocationUiState()
    object Loading : GeoLocationUiState()
    data class Success(val location: GeoLocation) : GeoLocationUiState()
    data class Error(val message: String) : GeoLocationUiState()
}