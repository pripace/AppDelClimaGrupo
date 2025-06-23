package com.example.appdeclima.repository

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appdeclima.repository.modelos.Ciudad
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RepositorioClimaTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repositorio: RepositorioClima
    private val mockApplication = mockk<Application>(relaxed = true)

    @Before
    fun setup() {
        repositorio = RepositorioClima(mockApplication)
    }

    @Test
    fun `obtener ciudades devuelve lista no vacia`() = runTest {
        val ciudades = repositorio.obtenerCiudades()
        assertNotNull(ciudades)
        assertTrue("La lista de ciudades está vacía", ciudades.isNotEmpty())
    }

    @Test
    fun `buscar ciudades con texto vacio devuelve lista vacia`() = runTest {
        val resultado = repositorio.buscarCiudades("")
        assertNotNull(resultado)
        assertTrue("Se esperaba una lista vacía", resultado.isEmpty())
    }

    @Test
    fun `obtener clima devuelve datos validos`() = runTest {
        val ciudad = Ciudad("Buenos Aires", "AR", -34.6118, -58.3960)
        val clima = repositorio.obtenerClima(ciudad)

        assertNotNull(clima)
        assertEquals("Buenos Aires", clima.ciudad.nombre)
        assertTrue("Temperatura inválida", clima.temperatura > -50)
        assertTrue("Humedad inválida", clima.humedad in 0..100)
    }
}

