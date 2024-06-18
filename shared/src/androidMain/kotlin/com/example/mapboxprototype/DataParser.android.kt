package com.example.mapboxprototype

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

actual object SensorDataParser {
    actual suspend fun parseCsv(filePath: String, week: String): List<SensorData> = withContext(Dispatchers.IO) {
        val inputStream = this::class.java.classLoader?.getResourceAsStream(filePath)
            ?: throw IllegalArgumentException("File not found: $filePath")
        val reader = InputStreamReader(inputStream)

        reader.useLines { lines ->
            lines.drop(1) // Skip header line
                .filter { it.isNotBlank() && it.split(",").last() == week }
                .map { line ->
                    val values = line.split(",")
                    mapToSensorData(values)
                }
                .toList()
        }
    }
}