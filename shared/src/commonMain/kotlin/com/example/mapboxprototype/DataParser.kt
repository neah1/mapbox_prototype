package com.example.mapboxprototype

expect object SensorDataParser {
    suspend fun parseCsv(filePath: String): List<SensorData>
}

suspend fun retrieveAndAggregateValues(
        week: String,
        selectedSensor: String,
        aggregateType: String,
        virtualSensorSubtype: String? = null,
        geohashResolution: Int
): Map<String, Double> {
    val data = SensorDataParser.parseCsv("iaq_data.csv")
    val filteredData = data.filter { it.week == week }

    val column = if (selectedSensor in listOf("virusrisk", "occupancy")) {
        "vs_${selectedSensor}_${virtualSensorSubtype}_$aggregateType"
    } else {
        "${selectedSensor}_$aggregateType"
    }

    val truncatedGeohashData = filteredData.mapNotNull { row ->
        val propertyGetterMap = getPropertyGetterMap(row)
        val value = propertyGetterMap[column]
        if (value != null) {
            val truncatedGeohash = truncateGeohash(row.geohash, geohashResolution)
            Pair(value, truncatedGeohash)
        } else {
            null
        }
    }

    return aggregateValues(truncatedGeohashData, aggregateType)
}

fun aggregateValues(
        data: List<Pair<Double, String>>,
        aggregateFunction: String
): Map<String, Double> {
    val groupedData = data.groupBy { it.second }
    return groupedData.mapValues { (_, values) ->
        val valuesList = values.map { it.first }
        when (aggregateFunction) {
            "average" -> valuesList.average()
            "max" -> valuesList.maxOrNull() ?: 0.0
            "min" -> valuesList.minOrNull() ?: 0.0
            else -> throw IllegalArgumentException("Unknown aggregate function: $aggregateFunction")
        }
    }
}

fun truncateGeohash(geohash: String, resolution: Int): String {
    return if (resolution in 1..geohash.length) {
        geohash.substring(0, resolution)
    } else {
        geohash
    }
}


