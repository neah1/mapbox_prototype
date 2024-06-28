package com.example.mapboxprototype

//@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
//actual object SensorDataParser {
//    actual suspend fun parseCsv(filePath: String): List<SensorData> = withContext(Dispatchers.Default) {
//        val fileContent = NSString.stringWithContentsOfFile(filePath, NSUTF8StringEncoding, null) as String
//        val lines = fileContent.split("\n")
//        val dataLines = lines.drop(1)
//
//        dataLines.map { line ->
//            val values = line.split(",")
//            mapToSensorData(values)
//        }
//    }
//}

actual object SensorDataParser {
    actual suspend fun parseCsv(filePath: String, column:String, week: String): List<Pair<Double, String>> {
        TODO("Not yet implemented")
    }

}