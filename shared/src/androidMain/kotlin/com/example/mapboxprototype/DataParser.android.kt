package com.example.mapboxprototype

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

actual object SensorDataParser {
    actual suspend fun parseCsv(filePath: String, column: String, week: String): List<Pair<Double, String>> = withContext(Dispatchers.IO) {
        val inputStream = this::class.java.classLoader?.getResourceAsStream(filePath)
            ?: throw IllegalArgumentException("File not found: $filePath")
        val reader = InputStreamReader(inputStream)

        logMemoryUsage("Before parsing")

        val res = reader.useLines { lines ->
            lines
                .drop(1) // Skip header line
                .filter { it.isNotBlank() && it.split(",").last() == week }
                .take(100)
                .mapNotNull { line ->
                    val values = line.split(",")
                    val row = mapToSensorData(values)
                    val value = getPropertyGetterMap(row)[column]
                    if (value != null && row.geohash.isNotEmpty()) {
                        Pair(value, row.geohash)
                    } else {
                        null
                    }
                }
                .toList() // Collect to a list
        }

        logMemoryUsage("After parsing")

        res
    }

    private fun logMemoryUsage(stage: String) {
        val runtime = Runtime.getRuntime()
        val usedMemory = runtime.totalMemory() - runtime.freeMemory()
        println("$stage - Used Memory: ${usedMemory / 1024 / 1024} MB")
    }
}