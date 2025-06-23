package com.example.appdeclima.presentacion.ciudades

import com.example.appdeclima.repository.modelos.Ciudad

sealed class IntencionCiudades {
    object CargarCiudades : IntencionCiudades()
    data class SeleccionarCiudad(val ciudad: Ciudad) : IntencionCiudades()
    data class BuscarCiudad(val texto: String) : IntencionCiudades()
    object BuscarPorUbicacion : IntencionCiudades()
}