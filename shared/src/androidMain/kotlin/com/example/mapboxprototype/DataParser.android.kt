package com.example.mapboxprototype

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

actual object SensorDataParser {
    actual suspend fun parseCsv(filePath: String): List<SensorData> = withContext(Dispatchers.IO) {
        val file = File(filePath)
        val lines = file.readLines()
        val dataLines = lines.drop(1)

        dataLines.map { line ->
            val values = line.split(",")
            mapToSensorData(values)
        }
    }
}