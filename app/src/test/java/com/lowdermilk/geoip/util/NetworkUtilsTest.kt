package com.lowdermilk.geoip.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NetworkUtilsTest {

    @Test
    fun testValidIPv4() {
        val validIPs = listOf(
            "192.168.1.1",
            "10.0.0.1",
            "172.16.0.1",
            "0.0.0.0",
            "255.255.255.255",
            "127.0.0.1"
        )
        for (ip in validIPs) {
            assertTrue("Should be valid IPv4: $ip", NetworkUtils.isIPv4(ip))
            assertTrue("Should be valid IP: $ip", NetworkUtils.isValidIp(ip))
        }
    }

    @Test
    fun testInvalidIPv4() {
        val invalidIPs = listOf(
            "256.256.256.256",
            "192.168.1",
            "192.168.1.1.1",
            "192.168.1.a",
            "192.168.1.256",
            "a.b.c.d",
            "192.168.01.1",
            ""
        )
        for (ip in invalidIPs) {
            assertFalse("Should be invalid IPv4: $ip", NetworkUtils.isIPv4(ip))
        }
    }

    @Test
    fun testValidIPv6() {
        val validIPv6s = listOf(
            "2001:0db8:85a3:0000:0000:8a2e:0370:7334",
            "2001:db8:85a3:0:0:8A2e:370:7334",
            "2001:db8:85a3::8A2e:370:7334",
            "::1",
            "::",
            "fe80::1",
            "::ffff:192.168.0.1",
            "2001:db8::1"
        )
        for (ip in validIPv6s) {
            assertTrue("Should be valid IPv6: $ip", NetworkUtils.isIPv6(ip))
            assertTrue("Should be valid IP: $ip", NetworkUtils.isValidIp(ip))
        }
    }

    @Test
    fun testInvalidIPv6() {
        val invalidIPv6s = listOf(
            "2001:0db8:85a3:::8a2e:0370:7334",
            "2001:0db8:85a3:0000:0000:8a2e:0370:7334:extra",
            "2001::85a3::8a2e",
            ":::1",
            "12345::1",
            "g:h:i:j:k:l:m:n",
            "2001:db8::1::2",
            "1:2:3:4:5:6:7:8:9",
            ""
        )
        for (ip in invalidIPv6s) {
            assertFalse("Should be invalid IPv6: $ip", NetworkUtils.isIPv6(ip))
        }
    }

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