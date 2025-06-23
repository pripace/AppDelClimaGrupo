package com.example.appdeclima.presentacion.clima

import com.example.appdeclima.repository.modelos.Ciudad

sealed class IntencionClima {
    data class CargarClima(val ciudad: Ciudad) : IntencionClima()
    data class CargarPronostico(val ciudad: Ciudad) : IntencionClima()
    object VolverACiudades : IntencionClima()
}