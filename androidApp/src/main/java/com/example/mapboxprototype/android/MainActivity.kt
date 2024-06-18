package com.example.mapboxprototype.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.mapboxprototype.AggregateFunction
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Create a map programmatically and set the initial camera
        mapView = MapView(this)
        mapView.mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(Point.fromLngLat(-98.0, 39.5))
                .pitch(0.0)
                .zoom(2.0)
                .bearing(0.0)
                .build()
        )
        // Add the map view to the activity (you can also add it to other views as a child)
        setContentView(mapView)

        retrieveAndDisplayGeohashData()
    }

    private fun retrieveAndDisplayGeohashData() {
        lifecycleScope.launch {
            val geohashData = com.example.mapboxprototype.getGeohashData(
                "2023-48",
                "radon",
                AggregateFunction.MAX,
                null,
                5
            )

            withContext(Dispatchers.Main) {
                displayGeohashDataOnMap(geohashData)
            }
        }
    }

    private fun displayGeohashDataOnMap(geohashData: Map<String, Double>) {
        // Create a PointAnnotationManager for adding points to the map
        val pointAnnotationManager: PointAnnotationManager = mapView.annotations.createPointAnnotationManager()

        for ((geohash, value) in geohashData) {
            // Decode the geohash to a Point (latitude, longitude)
            val point = decodeGeohash(geohash)
            val annotationOptions = PointAnnotationOptions()
                .withPoint(point)
                .withTextField(value.toString())
            pointAnnotationManager.create(annotationOptions)
        }
    }

    private fun decodeGeohash(geohash: String): Point {
        // This is just an example implementation. Replace with a real geohash decoding function.
        val decoded = when (geohash) {
            "9xj" -> Point.fromLngLat(-98.0, 39.5)
            "9xp" -> Point.fromLngLat(-97.0, 40.0)
            "9xq" -> Point.fromLngLat(-96.0, 41.0)
            else -> Point.fromLngLat(-98.0, 39.5)
        }
        return decoded
    }
}