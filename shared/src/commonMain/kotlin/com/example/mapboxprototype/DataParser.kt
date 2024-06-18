package com.example.mapboxprototype

expect object SensorDataParser {
    suspend fun parseCsv(filePath: String, week: String): List<SensorData>
}

fun truncateGeohash(geohash: String, resolution: Int): String {
    require(resolution > 0) { "Resolution must be greater than zero" }
    require(geohash.matches(Regex("^[0-9a-zA-Z]+$"))) { "Invalid geohash format: $geohash" }

    return if (resolution >= geohash.length) {
        geohash
    } else {
        geohash.substring(0, resolution)
    }
}


fun aggregateValues(
        data: List<Pair<Double, String>>,
        aggregateFunction: AggregateFunction
): Map<String, Double> {
    val groupedData = data.groupBy { it.second }
    return groupedData.mapValues { (_, values) ->
        val valuesList = values.map { it.first }
        when (aggregateFunction) {
            AggregateFunction.AVERAGE -> valuesList.average()
            AggregateFunction.MAX -> valuesList.maxOrNull() ?: 0.0
            AggregateFunction.MIN -> valuesList.minOrNull() ?: 0.0
        }
    }
}

suspend fun getGeohashData(
    week: String,
    selectedSensor: SensorType,
    aggregateType: AggregateFunction,
    virtualSensorSubtype: VirtualSensorType? = null,
    geohashResolution: Int
): Map<String, Double> {
    val data = SensorDataParser.parseCsv("iaq_data.csv", week)

    val column = when (selectedSensor) {
        SensorType.VIRUS_RISK, SensorType.OCCUPANCY -> "vs_${selectedSensor.name.lowercase()}_${virtualSensorSubtype?.name?.lowercase()}_${aggregateType.name.lowercase()}"
        else -> "${selectedSensor.name.lowercase()}_${aggregateType.name.lowercase()}"
    }

    val truncatedGeohashData = data.mapNotNull { row ->
        val propertyGetterMap = getPropertyGetterMap(row)
        val value = propertyGetterMap[column]
        if (value != null && row.geohash.isNotEmpty()) {
            val truncatedGeohash = truncateGeohash(row.geohash, geohashResolution)
            Pair(value, truncatedGeohash)
        } else {
            null
        }
    }

    return aggregateValues(truncatedGeohashData, aggregateType)
}