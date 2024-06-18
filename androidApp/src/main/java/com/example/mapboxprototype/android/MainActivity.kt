package com.example.mapboxprototype.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.mapboxprototype.AggregateFunction
import com.example.mapboxprototype.SensorType
import com.example.mapboxprototype.getGeohashAreaInfo
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
                SensorType.RADON,
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
            val (point, area) = getGeohashAreaInfo(geohash)
            val annotationOptions = PointAnnotationOptions()
                .withPoint(Point.fromLngLat(point.first, point.second))
                .withTextField(value.toString())
            pointAnnotationManager.create(annotationOptions)
        }
    }

}