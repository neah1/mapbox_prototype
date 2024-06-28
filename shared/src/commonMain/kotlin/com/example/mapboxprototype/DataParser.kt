package com.example.mapboxprototype

expect object SensorDataParser {
    suspend fun parseCsv(filePath: String, column:String, week: String): List<Pair<Double, String>>
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
    val column = when (selectedSensor) {
        SensorType.VIRUS_RISK, SensorType.OCCUPANCY -> "vs_${selectedSensor.name.lowercase()}_${virtualSensorSubtype?.name?.lowercase()}_${aggregateType.name.lowercase()}"
        else -> "${selectedSensor.name.lowercase()}_${aggregateType.name.lowercase()}"
    }

    val data = SensorDataParser.parseCsv("iaq_data.csv", column, week)

    val truncatedGeohashData = data.map { (value, geohash) ->
        Pair(value, truncateGeohash(geohash, geohashResolution)) }

    return aggregateValues(truncatedGeohashData, aggregateType)
}