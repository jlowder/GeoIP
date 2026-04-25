package com.example.ipgeolookup.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NetworkUtilsTest {

    @Test
    fun testValidIPv4() {
        assertTrue(NetworkUtils.isValidIp("192.168.1.1"))
        assertTrue(NetworkUtils.isValidIp("8.8.8.8"))
        assertTrue(NetworkUtils.isValidIp("127.0.0.1"))
        assertTrue(NetworkUtils.isValidIp("0.0.0.0"))
        assertTrue(NetworkUtils.isValidIp("255.255.255.255"))
    }

    @Test
    fun testInvalidIPv4() {
        assertFalse("Should be invalid: 256.256.256.256", NetworkUtils.isValidIp("256.256.256.256"))
        assertFalse("Should be invalid: 1.2.3", NetworkUtils.isValidIp("1.2.3"))
        assertFalse("Should be invalid: 1.2.3.4.5", NetworkUtils.isValidIp("1.2.3.4.5"))
    }

    @Test
    fun testValidIPv6() {
        assertTrue(NetworkUtils.isValidIp("2001:0db8:85a3:0000:0000:8a2e:0370:7334"))
        assertTrue(NetworkUtils.isValidIp("2001:db8:85a3::8a2e:370:7334"))
        assertTrue(NetworkUtils.isValidIp("::1"))
        assertTrue(NetworkUtils.isValidIp("fe80::1"))
        assertTrue(NetworkUtils.isValidIp("::ffff:192.0.2.1"))
        assertTrue(NetworkUtils.isValidIp("2001:DB8::1")) // Case insensitive
    }

    @Test
    fun testInvalidIPv6() {
        assertFalse(NetworkUtils.isValidIp("2001:db8:::1"))
        assertFalse(NetworkUtils.isValidIp("12345::1"))
        assertFalse(NetworkUtils.isValidIp("g:h:i:j:k:l:m:n"))
    }

    @Test
    fun testValidHostnames() {
        assertTrue(NetworkUtils.isValidIp("google.com"))
        assertTrue(NetworkUtils.isValidIp("www.google.com"))
        assertTrue(NetworkUtils.isValidIp("localhost"))
        assertTrue(NetworkUtils.isValidIp("my-server.local"))
        assertTrue(NetworkUtils.isValidIp("a.b.c.d.e.f.g"))
    }

    @Test
    fun testInvalidHostnames() {
        assertFalse(NetworkUtils.isValidIp("-google.com"))
        assertFalse(NetworkUtils.isValidIp("google-.com"))
        assertFalse(NetworkUtils.isValidIp("google..com"))
        assertFalse(NetworkUtils.isValidIp("google.com/path"))
    }
}
