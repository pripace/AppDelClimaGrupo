package com.example.appdeclima.repository.modelos

import kotlinx.serialization.Serializable

@Serializable
data class Ciudad(
    val name: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val state: String? = null
) {
    val nombre: String get() = name
    val pais: String get() = country
}