package com.example.ipgeolookup.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NetworkUtilsTest {

    @Test
    fun testValidIPv4() {
        val validIps = listOf(
            "192.168.1.1",
            "8.8.8.8",
            "127.0.0.1",
            "0.0.0.0",
            "255.255.255.255",
            "1.2.3.4"
        )
        for (ip in validIps) {
            assertTrue("Should be valid IPv4: $ip", NetworkUtils.isIPv4(ip))
            assertTrue("Should be valid general IP: $ip", NetworkUtils.isValidIp(ip))
        }
    }

    @Test
    fun testInvalidIPv4() {
        val invalidIps = listOf(
            "256.256.256.256",
            "1.2.3",
            "1.2.3.4.5",
            "192.168.1.256",
            "a.b.c.d",
            "192.168.01.1", // Technically invalid in many implementations due to octal ambiguity
            ""
        )
        for (ip in invalidIps) {
            assertFalse("Should be invalid IPv4: $ip", NetworkUtils.isIPv4(ip))
        }
    }

    @Test
    fun testValidIPv6() {
        val validIps = listOf(
            "2001:0db8:85a3:0000:0000:8a2e:0370:7334", // Full
            "2001:db8:85a3:0:0:8a2e:370:7334",       // Shortened
            "2001:db8:85a3::8a2e:370:7334",         // Compressed
            "2001:db8::",                           // Compressed end
            "::1",                                  // Loopback
            "::",                                   // Unspecified
            "fe80::1",                              // Link-local
            "ff02::1",                              // Multicast
            "::ffff:192.168.0.1",                   // IPv4-mapped
            "2001:DB8::1"                           // Case insensitive
        )
        for (ip in validIps) {
            assertTrue("Should be valid IPv6: $ip", NetworkUtils.isIPv6(ip))
            assertTrue("Should be valid general IP: $ip", NetworkUtils.isValidIp(ip))
        }
    }

    @Test
    fun testInvalidIPv6() {
        val invalidIps = listOf(
            "2001:db8:::1",      // Too many colons
            "12345::1",          // Too many hex digits
            "g:h:i:j:k:l:m:n",   // Invalid hex
            "2001:db8::1::2",    // Multiple compressions
            "1:2:3:4:5:6:7:8:9", // Too many parts
            ""
        )
        for (ip in invalidIps) {
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
