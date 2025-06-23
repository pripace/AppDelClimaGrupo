package com.example.appdeclima.presentacion.clima

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.appdeclima.repository.RepositorioClima
import com.example.appdeclima.repository.modelos.Ciudad

class ClimaViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = RepositorioClima(application)

    var estado by mutableStateOf<EstadoClima>(EstadoClima.Cargando)
        private set

    fun manejarIntencion(intencion: IntencionClima) {
        when (intencion) {
            is IntencionClima.CargarClima -> cargarClima(intencion.ciudad)
            is IntencionClima.CargarPronostico -> cargarPronostico(intencion.ciudad)
            is IntencionClima.VolverACiudades -> {}
        }
    }

    private fun cargarClima(ciudad: Ciudad) {
        estado = EstadoClima.Cargando
        viewModelScope.launch {
            try {
                val clima = repositorio.obtenerClima(ciudad)
                estado = EstadoClima.Exito(clima)
                cargarPronostico(ciudad)
            } catch (e: Exception) {
                estado = EstadoClima.Error("Error al cargar el clima")
            }
        }
    }

    private fun cargarPronostico(ciudad: Ciudad) {
        viewModelScope.launch {
            try {
                val pronostico = repositorio.obtenerPronostico(ciudad)
                when (val estadoActual = estado) {
                    is EstadoClima.Exito -> {
                        estado = estadoActual.copy(pronostico = pronostico)
                    }
                    else -> {}
                }
            } catch (e: Exception) {

            }
        }
    }
}