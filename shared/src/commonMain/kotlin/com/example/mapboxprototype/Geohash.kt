package com.example.mapboxprototype

fun decodeGeohash(geohash: String): Pair<Double, Double> {
    require(geohash.matches(Regex("^[0-9a-zA-Z]+$"))) { "Invalid geohash format: $geohash" }
    val base32 = "0123456789bcdefghjkmnpqrstuvwxyz"
    var even = true
    val latRange = doubleArrayOf(-90.0, 90.0)
    val lonRange = doubleArrayOf(-180.0, 180.0)

    for (c in geohash.toCharArray()) {
        val cd = base32.indexOf(c)
        for (mask in intArrayOf(16, 8, 4, 2, 1)) {
            if (even) {
                refineInterval(lonRange, cd, mask)
            } else {
                refineInterval(latRange, cd, mask)
            }
            even = !even
        }
    }
    val lat = (latRange[0] + latRange[1]) / 2
    val lon = (lonRange[0] + lonRange[1]) / 2
    return Pair(lon, lat)
}

private fun refineInterval(interval: DoubleArray, cd: Int, mask: Int) {
    if ((cd and mask) != 0) {
        interval[0] = (interval[0] + interval[1]) / 2
    } else {
        interval[1] = (interval[0] + interval[1]) / 2
    }
}

fun calculateGeohashAreaSize(resolution: Int): Pair<Double, Double> {
    // Approximate the size of the area represented by a geohash with given resolution.
    // This is a rough approximation and can vary depending on latitude.
    require(resolution in 1..12) { "Resolution must be between 1 and 12" }

    val cellSizes = listOf(
        Pair(5000.0, 5000.0),      // 1
        Pair(1250.0, 625.0),       // 2
        Pair(156.0, 156.0),        // 3
        Pair(39.1, 19.5),          // 4
        Pair(4.89, 4.89),          // 5
        Pair(1.22, 0.61),          // 6
        Pair(0.153, 0.153),        // 7
        Pair(0.0382, 0.0191),      // 8
        Pair(0.00477, 0.00477),    // 9
        Pair(0.00119, 0.000596),   // 10
        Pair(0.000149, 0.000149),  // 11
        Pair(0.0000372, 0.0000186) // 12
    )

    return cellSizes[resolution - 1]
}

fun getGeohashAreaInfo(geohash: String): Pair<Pair<Double, Double>, Pair<Double, Double>> {
    val center = decodeGeohash(geohash)
    val areaSize = calculateGeohashAreaSize(geohash.length)
    return Pair(center, areaSize)
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