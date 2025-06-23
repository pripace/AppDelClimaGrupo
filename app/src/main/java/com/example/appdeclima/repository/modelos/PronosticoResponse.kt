package com.example.appdeclima.repository.modelos


import kotlinx.serialization.Serializable

@Serializable
data class PronosticoResponse(
    val list: List<PronosticoItem>
)

@Serializable
data class PronosticoItem(
    val main: Main,
    val weather: List<Weather>,
    val dt_txt: String
)

@Serializable
data class PronosticoDia(
    val fecha: String,
    val temperaturaMax: Int,
    val temperaturaMin: Int,
    val descripcion: String
)