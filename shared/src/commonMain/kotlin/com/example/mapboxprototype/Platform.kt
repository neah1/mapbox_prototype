package com.example.mapboxprototype

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform