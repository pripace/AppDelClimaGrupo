package com.example.appdeclima.repository.modelos

data class Clima(
    val temperatura: Int,
    val descripcion: String,
    val humedad: Int,
    val ciudad: Ciudad
)