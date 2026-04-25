package com.example.ipgeolookup.data.repository

import org.junit.Assert.assertNotNull
import org.junit.Test
import java.net.InetAddress

class HostnameResolutionTest {

    @Test
    fun testHostnameResolution() {
        val hostnames = listOf("google.com", "github.com", "example.com")
        for (hostname in hostnames) {
            val address = InetAddress.getByName(hostname)
            println("$hostname resolves to ${address.hostAddress}")
            assertNotNull("Failed to resolve $hostname", address.hostAddress)
        }
    }
}
