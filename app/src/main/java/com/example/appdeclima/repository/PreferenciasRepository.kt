package com.example.appdeclima.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.appdeclima.repository.modelos.Ciudad
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PreferenciasRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("clima_prefs", Context.MODE_PRIVATE)

    suspend fun guardarUltimaCiudad(ciudad: Ciudad) {
        withContext(Dispatchers.IO) {
            prefs.edit().apply {
                putString("ultima_ciudad_nombre", ciudad.nombre)
                putString("ultima_ciudad_pais", ciudad.pais)
                putFloat("ultima_ciudad_lat", ciudad.lat.toFloat())
                putFloat("ultima_ciudad_lon", ciudad.lon.toFloat())
                putString("ultima_ciudad_state", ciudad.state)
                apply()
            }
        }
    }

    suspend fun obtenerUltimaCiudad(): Ciudad? {
        return withContext(Dispatchers.IO) {
            null
        }
    }

    suspend fun limpiarUltimaCiudad() {
        withContext(Dispatchers.IO) {
            prefs.edit().clear().apply()
        }
    }
}
