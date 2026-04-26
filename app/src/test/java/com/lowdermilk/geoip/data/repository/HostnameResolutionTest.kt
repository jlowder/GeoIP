package com.lowdermilk.geoip.data.repository

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

    @Test
    fun testIpResolution() {
        val ips = listOf("8.8.8.8", "1.1.1.1", "2001:4860:4860::8888")
        for (ip in ips) {
            val address = InetAddress.getByName(ip)
            println("$ip resolves to ${address.hostAddress}")
            assertNotNull("Failed to resolve $ip", address.hostAddress)
        }
    }
}
