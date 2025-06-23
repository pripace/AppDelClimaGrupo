package com.example.appdeclima.repository

import android.content.Context
import com.example.appdeclima.repository.modelos.Ciudad
import com.example.appdeclima.repository.modelos.Clima
import com.example.appdeclima.repository.modelos.PronosticoDia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositorioClima(context: Context) {

    private val repositorioApi = RepositorioApi()
    private val preferenciasRepository = PreferenciasRepository(context)

    private val ciudadesHardcodeadas = listOf(
        Ciudad("Buenos Aires", "AR", -34.6118, -58.3960),
        Ciudad("Rosario", "AR", -32.9468, -60.6393),
        Ciudad("Córdoba", "AR", -31.4201, -64.1888),
        Ciudad("Salta", "AR", -24.7829, -65.4232),
        Ciudad("Ushuaia", "AR", -54.8019, -68.3030)
    )

    suspend fun obtenerCiudades(): List<Ciudad> {
        return withContext(Dispatchers.IO) {
            preferenciasRepository.limpiarUltimaCiudad()
            ciudadesHardcodeadas
        }
    }

    suspend fun buscarCiudades(query: String): List<Ciudad> {
        return withContext(Dispatchers.IO) {
            try {
                repositorioApi.buscarCiudades(query)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    suspend fun obtenerCiudadPorCoordenadas(lat: Double, lon: Double): Ciudad? {
        return withContext(Dispatchers.IO) {
            try {
                preferenciasRepository.limpiarUltimaCiudad()
                val ciudades = repositorioApi.obtenerCiudadPorCoordenadas(lat, lon)
                ciudades.firstOrNull()
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun obtenerClima(ciudad: Ciudad): Clima {
        return withContext(Dispatchers.IO) {
            try {
                val response = repositorioApi.obtenerClimaActual(ciudad.lat, ciudad.lon)
                Clima(
                    temperatura = response.main.temp.toInt(),
                    descripcion = response.weather.firstOrNull()?.description ?: "Sin datos",
                    humedad = response.main.humidity,
                    ciudad = ciudad
                )
            } catch (e: Exception) {
                Clima(
                    temperatura = 25,
                    descripcion = "Error al cargar",
                    humedad = 60,
                    ciudad = ciudad
                )
            }
        }
    }

    suspend fun obtenerPronostico(ciudad: Ciudad): List<PronosticoDia> {
        return withContext(Dispatchers.IO) {
            try {
                val response = repositorioApi.obtenerPronostico(ciudad.lat, ciudad.lon)
                val pronosticosPorDia = response.list
                    .groupBy { it.dt_txt.substring(0, 10) }
                    .entries.take(7)
                    .map { (fecha, items) ->
                        val tempMax = items.maxOf { it.main.temp.toInt() }
                        val tempMin = items.minOf { it.main.temp.toInt() }
                        val descripcion = items.first().weather.firstOrNull()?.description ?: "Sin datos"

                        PronosticoDia(
                            fecha = fecha,
                            temperaturaMax = tempMax,
                            temperaturaMin = tempMin,
                            descripcion = descripcion
                        )
                    }
                pronosticosPorDia
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}