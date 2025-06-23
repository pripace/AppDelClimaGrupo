package com.example.appdeclima.repository.modelos

import kotlinx.serialization.Serializable

@Serializable
data class ClimaResponse(
    val main: Main,
    val weather: List<Weather>,
    val name: String
)

@Serializable
data class Main(
    val temp: Double,
    val humidity: Int,
    val temp_min: Double,
    val temp_max: Double
)

@Serializable
data class Weather(
    val main: String,
    val description: String
)
