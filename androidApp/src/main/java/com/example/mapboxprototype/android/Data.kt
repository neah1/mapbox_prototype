package com.example.mapboxprototype.android

import com.example.mapboxprototype.retrieveAndAggregateValues

suspend fun getGeohashData() {
    val week = "2023-48" // Example week
    val selectedSensor = "radon" // Example sensor
    val aggregateType = "max" // Example aggregate type
    val geohashResolution = 5 // Example geohash resolution

    val results = retrieveAndAggregateValues(
            week,
            selectedSensor,
            aggregateType,
            null,
            geohashResolution,
    )

    results.forEach { (geohash, value) ->
        println("Geohash: $geohash, Value: $value")
    }

}