package com.example.appdeclima.presentacion.ciudades

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class CiudadesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()
    private lateinit var viewModel: CiudadesViewModel
    private val mockApplication = mockk<Application>(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = CiudadesViewModel(mockApplication)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cuando se carga ciudades entonces estado cambia a cargando`() = runTest {
        viewModel.manejarIntencion(IntencionCiudades.CargarCiudades)
        dispatcher.scheduler.advanceUntilIdle()
        assertTrue(viewModel.estado is EstadoCiudades.Cargando)
    }

    @Test
    fun `cuando se busca ciudad vacia entonces carga ciudades por defecto`() = runTest {
        viewModel.manejarIntencion(IntencionCiudades.BuscarCiudad(""))
        dispatcher.scheduler.advanceUntilIdle()
        assertTrue(viewModel.estado is EstadoCiudades.Cargando)
    }

    @Test
    fun `cuando se busca por ubicacion entonces estado cambia a cargando`() = runTest {
        viewModel.manejarIntencion(IntencionCiudades.BuscarPorUbicacion)
        dispatcher.scheduler.advanceUntilIdle()
        assertTrue(viewModel.estado is EstadoCiudades.Cargando)
    }
}
