
import com.example.mapboxprototype.AggregateFunction
import com.example.mapboxprototype.SensorType
import com.example.mapboxprototype.aggregateValues
import com.example.mapboxprototype.getGeohashData
import com.example.mapboxprototype.truncateGeohash
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class SensorDataParserTest {

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
    fun testAggregateValuesAverage() {
        val data = listOf(Pair(10.0, "geohash1"), Pair(20.0, "geohash1"), Pair(30.0, "geohash2"))
        val aggregated = aggregateValues(data, AggregateFunction.AVERAGE)
        assertEquals(mapOf("geohash1" to 15.0, "geohash2" to 30.0), aggregated)
    }

    @Test
    fun testAggregateValuesMax() {
        val data = listOf(Pair(10.0, "geohash1"), Pair(20.0, "geohash1"), Pair(30.0, "geohash2"))
        val aggregated = aggregateValues(data, AggregateFunction.MAX)
        assertEquals(mapOf("geohash1" to 20.0, "geohash2" to 30.0), aggregated)
    }

    @Test
    fun testAggregateValuesMin() {
        val data = listOf(Pair(10.0, "geohash1"), Pair(20.0, "geohash1"), Pair(30.0, "geohash2"))
        val aggregated = aggregateValues(data, AggregateFunction.MIN)
        assertEquals(mapOf("geohash1" to 10.0, "geohash2" to 30.0), aggregated)
    }

    @Test
    fun testGetGeohashData() = runBlocking {
        val result = getGeohashData(
            week = "2023-48",
            selectedSensor = SensorType.RADON,
            aggregateType = AggregateFunction.AVERAGE,
            geohashResolution = 3
        )

        println(result.toString())

        assertNotNull(result)
        assertEquals(1141, result.size)
        assertEquals(110.7478109452735, result["9x0"])
        assertEquals(103.06778133981865, result["ccg"])
    }
}
