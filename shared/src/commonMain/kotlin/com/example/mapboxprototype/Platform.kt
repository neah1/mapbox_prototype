package com.example.mapboxprototype

expect class DatabaseClient {
    suspend fun fetchData(query: String, parameters: Map<String, Any>): List<Map<String, Any>>
}