package com.lowdermilk.geoip.data.repository

import com.lowdermilk.geoip.util.NetworkUtils
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HostnameResolutionTest {

    @Test
    fun testValidHostnames() {
        val validHostnames = listOf(
            "google.com",
            "www.google.com",
            "localhost",
            "my-server.local",
            "a.b.c.d.e.f.g",
            "123-abc.example.com",
            "xn--80ak6aa92e.com" // Punycode
        )
        for (name in validHostnames) {
            assertTrue("Should be valid hostname: $name", NetworkUtils.isHostname(name))
            assertTrue("Should be valid general IP/Host: $name", NetworkUtils.isValidIp(name))
        }
    }

    @Test
    fun testInvalidHostnames() {
        val invalidHostnames = listOf(
            "-google.com",
            "google-.com",
            "google..com",
            "google.com/path",
            "user@google.com",
            "google.com:80",
            ""
        )
        for (name in invalidHostnames) {
            assertFalse("Should be invalid hostname: $name", NetworkUtils.isHostname(name))
        }
    }
}