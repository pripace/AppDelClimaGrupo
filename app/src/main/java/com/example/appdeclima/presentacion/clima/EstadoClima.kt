package com.example.appdeclima.presentacion.clima

import com.example.appdeclima.repository.modelos.Clima
import com.example.appdeclima.repository.modelos.PronosticoDia

sealed class EstadoClima {
    object Cargando : EstadoClima()
    data class Exito(
        val clima: Clima,
        val pronostico: List<PronosticoDia> = emptyList()
    ) : EstadoClima()
    data class Error(val mensaje: String) : EstadoClima()
}