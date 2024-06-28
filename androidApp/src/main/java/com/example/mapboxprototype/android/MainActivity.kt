package com.example.mapboxprototype.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.mapboxprototype.AggregateFunction
import com.example.mapboxprototype.SensorType
import com.example.mapboxprototype.getGeohashAreaInfo
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.style.expressions.generated.Expression
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.fillExtrusionLayer
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
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
        setContentView(mapView)

        retrieveAndDisplayGeohashData()
    }

    private fun retrieveAndDisplayGeohashData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val geohashData = com.example.mapboxprototype.getGeohashData(
                "2023-48",
                SensorType.RADON,
                AggregateFunction.MAX,
                null,
                3
            )

            val geoJsonData = createGeoJsonData(geohashData)

            withContext(Dispatchers.Main) {
                // Add the GeoJSON data as a source
                mapView.mapboxMap.getStyle { style ->
                    style.addSource(geoJsonSource("geohash-source") {
                        featureCollection(geoJsonData)
                    })

                    // Add the FillExtrusionLayer
                    style.addLayer(fillExtrusionLayer("extrusion-layer", "geohash-source") {
                        fillExtrusionHeight(Expression.get("value"))
                        fillExtrusionBase(0.0)
                        fillExtrusionColor("#3bb2d0")
                        fillExtrusionOpacity(0.6)
                    })
                }
            }
        }
    }

    private fun createGeoJsonData(geohashData: Map<String, Double>): FeatureCollection {
        val features = geohashData.map { (geohash, value) ->
            val (point, area) = getGeohashAreaInfo(geohash)
            val polygonPoints = createPolygonVertices(point, area)

            Feature.fromGeometry(
                Polygon.fromLngLats(listOf(polygonPoints)),
                null,
                value.toString()
            )
        }
        return FeatureCollection.fromFeatures(features)
    }

    private fun createPolygonVertices(center: Pair<Double, Double>, area: Pair<Double, Double>): List<Point> {
        // Create a polygon around the center point with given width and height in km
        val (lon, lat) = center
        val (widthKm, heightKm) = area
        val widthDeg = widthKm / 111.32 // Convert width from km to degrees (approx)
        val heightDeg = heightKm / 110.57 // Convert height from km to degrees (approx)

        return listOf(
            Point.fromLngLat(lon - widthDeg / 2, lat - heightDeg / 2),
            Point.fromLngLat(lon + widthDeg / 2, lat - heightDeg / 2),
            Point.fromLngLat(lon + widthDeg / 2, lat + heightDeg / 2),
            Point.fromLngLat(lon - widthDeg / 2, lat + heightDeg / 2),
            Point.fromLngLat(lon - widthDeg / 2, lat - heightDeg / 2) // Closing the polygon
        )
    }

}