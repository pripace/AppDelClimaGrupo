package com.example.appdeclima.presentacion.ciudades

import com.example.appdeclima.repository.modelos.Ciudad

sealed class EstadoCiudades {
    object Cargando : EstadoCiudades()
    data class Exito(val ciudades: List<Ciudad>) : EstadoCiudades()
    data class Error(val mensaje: String) : EstadoCiudades()
}