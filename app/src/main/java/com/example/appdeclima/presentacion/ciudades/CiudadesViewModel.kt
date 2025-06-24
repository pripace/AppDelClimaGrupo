package com.example.appdeclima.presentacion.ciudades

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.appdeclima.repository.RepositorioClima

class CiudadesViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = RepositorioClima(application)

    var estado by mutableStateOf<EstadoCiudades>(EstadoCiudades.Cargando)
        private set

    fun manejarIntencion(intencion: IntencionCiudades) {
        when (intencion) {
            is IntencionCiudades.CargarCiudades -> cargarCiudades()
            is IntencionCiudades.BuscarCiudad -> buscarCiudad(intencion.texto)
            is IntencionCiudades.BuscarPorUbicacion -> buscarPorUbicacion()
        }
    }

    private fun cargarCiudades() {
        estado = EstadoCiudades.Cargando
        viewModelScope.launch {
            try {
                val ciudades = repositorio.obtenerCiudades()
                estado = EstadoCiudades.Exito(ciudades)
            } catch (e: Exception) {
                estado = EstadoCiudades.Error("Error al cargar ciudades")
            }
        }
    }

    private fun buscarCiudad(texto: String) {
        if (texto.isBlank()) {
            cargarCiudades()
            return
        }

        estado = EstadoCiudades.Cargando
        viewModelScope.launch {
            try {
                val ciudades = repositorio.buscarCiudades(texto)
                estado = EstadoCiudades.Exito(ciudades)
            } catch (e: Exception) {
                estado = EstadoCiudades.Error("Error al buscar ciudades")
            }
        }
    }

    private fun buscarPorUbicacion() {
        estado = EstadoCiudades.Cargando
    }

    fun manejarCoordenadas(lat: Double, lon: Double) {
        estado = EstadoCiudades.Cargando
        viewModelScope.launch {
            try {
                val ciudad = repositorio.obtenerCiudadPorCoordenadas(lat, lon)
                if (ciudad != null) {
                    estado = EstadoCiudades.Exito(listOf(ciudad))
                } else {
                    estado = EstadoCiudades.Error("No se pudo encontrar tu ubicación")
                }
            } catch (e: Exception) {
                estado = EstadoCiudades.Error("Error al obtener ubicación")
            }
        }
    }
}