package com.example.mapboxprototype

expect class DatabaseClient {
    fun connect(): Connection
    fun fetchData(query: String, parameters: Map<String, Any>): DataFrame
}

expect fun createMapView(context: Any): MapView

data class SensorData(
    val sensorType: String,
    val value: Double,
    val timestamp: Long,
    val geolocation: GeoPoint
)

data class GeoPoint(
    val latitude: Double,
    val longitude: Double
)

