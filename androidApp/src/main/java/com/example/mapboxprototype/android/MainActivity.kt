package com.example.mapboxprototype.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.mapboxprototype.AggregateFunction
import com.example.mapboxprototype.SensorType
import com.example.mapboxprototype.getGeohashAreaInfo
import com.google.gson.JsonObject
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.generated.Expression
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.fillExtrusionLayer
import com.mapbox.maps.extension.style.layers.generated.symbolLayer
import com.mapbox.maps.extension.style.layers.properties.generated.TextAnchor
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.terrain.generated.removeTerrain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.max

class MainActivity : ComponentActivity() {
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Create a map programmatically and set the initial camera
        mapView = MapView(this)
        mapView.mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(Point.fromLngLat(-98.0, 39.5))
                .pitch(30.0)
                .zoom(2.0)
                .bearing(0.0)
                .build()
        )
        setContentView(mapView)

        mapView.mapboxMap.apply {
            loadStyle(Style.DARK)
            retrieveAndDisplayGeohashData()
        }
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
                    style.removeTerrain()
                    style.addSource(geoJsonSource("geohash-source") {
                        featureCollection(geoJsonData)
                    })

                    // Add the FillExtrusionLayer
                    style.addLayer(fillExtrusionLayer("extrusion-layer", "geohash-source") {
                        fillExtrusionHeight(Expression.get("height"))
                        fillExtrusionBase(0.0)
                        fillExtrusionColor(Expression.get("color"))
                        fillExtrusionOpacity(0.6)
                    })

                    style.addLayer(symbolLayer("symbol-layer", "geohash-source") {
                        textField(Expression.get("value"))
                        textSize(12.0)
                        textColor("black")
                        textAnchor(TextAnchor.CENTER)
                        textAllowOverlap(true)
                    })
                }
            }
        }
    }

    private fun createGeoJsonData(geohashData: Map<String, Double>): FeatureCollection {
        val features = geohashData.map { (geohash, value) ->
            val (point, area) = getGeohashAreaInfo(geohash)
            val polygonPoints = createPolygonVertices(point, area)
            val resolution = geohash.length

            val scaledHeight = scaleHeight(value, resolution)
            val color = scaleColor(value)
            val properties = JsonObject().apply {
                addProperty("height", scaledHeight)
                addProperty("color", color)
                addProperty("value", value)
            }

            Feature.fromGeometry(Polygon.fromLngLats(listOf(polygonPoints)), properties)
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

    private fun scaleHeight(value: Double, resolution: Int): Double {
        val baseHeight = 100.0 // Base height for visibility
        val scaleFactor = max(1.0, 12.0 - resolution) // Adjust scale based on resolution
        return baseHeight + value * scaleFactor
    }

    private fun scaleColor(value: Double): String {
        val minValue = 0.0
        val maxValue = 100.0 // Adjust max value based on your data range
        val normalizedValue = (value - minValue) / (maxValue - minValue)
        val hue = (1.0 - normalizedValue) * 240.0 // Blue to Red
        return "hsl($hue, 100%, 50%)"
    }

}