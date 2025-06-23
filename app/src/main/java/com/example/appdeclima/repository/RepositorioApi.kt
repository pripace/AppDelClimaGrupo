package com.example.appdeclima.repository

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import com.example.appdeclima.repository.modelos.Ciudad
import com.example.appdeclima.repository.modelos.ClimaResponse
import com.example.appdeclima.repository.modelos.PronosticoResponse

class RepositorioApi {

    private val apiKey = "f3953bf7dbe04deea9b5bb4f3768fda6"

    private val cliente = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun buscarCiudades(query: String): List<Ciudad> {
        return cliente.get("https://api.openweathermap.org/geo/1.0/direct") {
            parameter("q", query)
            parameter("limit", 5)
            parameter("appid", apiKey)
        }.body()
    }

    suspend fun obtenerClimaActual(lat: Double, lon: Double): ClimaResponse {
        return cliente.get("https://api.openweathermap.org/data/2.5/weather") {
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("appid", apiKey)
            parameter("units", "metric")
            parameter("lang", "en")
        }.body()
    }

    suspend fun obtenerCiudadPorCoordenadas(lat: Double, lon: Double): List<Ciudad> {
        return cliente.get("https://api.openweathermap.org/geo/1.0/reverse") {
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("limit", 1)
            parameter("appid", apiKey)
        }.body()
    }

    suspend fun obtenerPronostico(lat: Double, lon: Double): PronosticoResponse {
        return cliente.get("https://api.openweathermap.org/data/2.5/forecast") {
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("appid", apiKey)
            parameter("units", "metric")
            parameter("lang", "en")
        }.body()
    }
}