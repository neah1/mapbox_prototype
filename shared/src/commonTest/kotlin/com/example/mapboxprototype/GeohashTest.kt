package com.example.mapboxprototype

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class GeohashTest {

    @Test
    fun testTruncateGeohash() {
        val geohash = "9xj1"
        val resolution = 3
        val truncated = truncateGeohash(geohash, resolution)
        assertEquals("9xj", truncated)
    }

    @Test
    fun testTruncateGeohashLonger() {
        val geohash = "9xj1"
        val resolution = 5
        val truncated = truncateGeohash(geohash, resolution)
        assertEquals("9xj1", truncated)
    }

    @Test
    fun testTruncateGeohashEqualLength() {
        val geohash = "9xj1"
        val resolution = 4
        val truncated = truncateGeohash(geohash, resolution)
        assertEquals("9xj1", truncated)
    }

    @Test
    fun testTruncateGeohashLessThanOne() {
        val geohash = "9xj1"
        val resolution = 0
        assertFailsWith<IllegalArgumentException> {
            truncateGeohash(geohash, resolution)
        }
    }

    @Test
    fun testTruncateGeohashNegativeResolution() {
        val geohash = "9xj1"
        val resolution = -1
        assertFailsWith<IllegalArgumentException> {
            truncateGeohash(geohash, resolution)
        }
    }

    @Test
    fun testTruncateGeohashInvalidFormat() {
        val geohash = "9xj1#"
        val resolution = 3
        assertFailsWith<IllegalArgumentException> {
            truncateGeohash(geohash, resolution)
        }
    }

    @Test
    fun testDecodeGeohash() {
        val geohash = "9xj1"
        val (lon, lat) = decodeGeohash(geohash)
        assertTrue { lon in -180.0..180.0 }
        assertTrue { lat in -90.0..90.0 }
        assertEquals(-105.29, lon, 0.01)
        assertEquals(39.64, lat, 0.01)
    }

    @Test
    fun testCalculateGeohashAreaSize() {
        val resolution = 5
        val areaSize = calculateGeohashAreaSize(resolution)
        assertTrue { areaSize.first > 0 && areaSize.second > 0 }
        assertEquals(4.89, areaSize.first, 0.01)  // Approximate value
        assertEquals(4.89, areaSize.second, 0.01)  // Approximate value
    }

    @Test
    fun testGetGeohashAreaInfo() {
        val geohash = "ccgr"
        val (point, areaSize) = getGeohashAreaInfo(geohash)
        assertTrue { point.first in -180.0..180.0 }
        assertTrue { point.second in -90.0..90.0 }
        assertTrue { areaSize.first > 0 && areaSize.second > 0 }
        assertEquals(-96.5, point.first, 0.01)
        assertEquals(56.16, point.second, 0.01)
        assertEquals(39.1, areaSize.first, 0.01)  // Approximate value
        assertEquals(19.5, areaSize.second, 0.01)  // Approximate value
    }

    @Test
    fun testInvalidGeohashFormat() {
        val invalidGeohash = "invalid#"
        val exception = kotlin.runCatching { decodeGeohash(invalidGeohash) }.exceptionOrNull()
        assertTrue { exception is IllegalArgumentException }
    }

    @Test
    fun testCalculateGeohashAreaSizeWithZeroResolution() {
        val exception = kotlin.runCatching { calculateGeohashAreaSize(0) }.exceptionOrNull()
        assertTrue { exception is IllegalArgumentException }
    }
}