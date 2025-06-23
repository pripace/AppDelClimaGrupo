package com.example.appdeclima.presentacion.clima

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appdeclima.repository.modelos.Ciudad
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
class ClimaViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ClimaViewModel
    private val mockApplication = mockk<Application>(relaxed = true)
    private val ciudadTest = Ciudad("Buenos Aires", "AR", -34.6118, -58.3960)

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = ClimaViewModel(mockApplication)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cuando se carga clima entonces estado cambia a cargando`() = runTest {
        viewModel.manejarIntencion(IntencionClima.CargarClima(ciudadTest))
        dispatcher.scheduler.advanceUntilIdle()
        assertTrue(viewModel.estado is EstadoClima.Cargando)
    }

    @Test
    fun `cuando se carga pronostico entonces mantiene estado actual`() = runTest {
        viewModel.manejarIntencion(IntencionClima.CargarPronostico(ciudadTest))
        dispatcher.scheduler.advanceUntilIdle()
        assertTrue(viewModel.estado is EstadoClima.Cargando)
    }
}

