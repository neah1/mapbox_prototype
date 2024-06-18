package com.example.mapboxprototype

expect object SensorDataParser {
    suspend fun parseCsv(filePath: String): List<SensorData>
}

fun truncateGeohash(geohash: String, resolution: Int): String = geohash.takeIf { resolution in 1..it.length }?.substring(0, resolution) ?: geohash


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
    selectedSensor: String,
    aggregateType: AggregateFunction,
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