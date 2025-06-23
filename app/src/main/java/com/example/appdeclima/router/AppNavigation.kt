package com.example.appdeclima.router

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appdeclima.presentacion.ciudades.CiudadesPage
import com.example.appdeclima.presentacion.clima.ClimaPage
import com.example.appdeclima.repository.modelos.Ciudad
import com.example.appdeclima.presentacion.ciudades.CiudadesViewModel

@Composable
fun AppNavigation(
    onCompartir: (String) -> Unit = {},
    onBuscarUbicacion: (CiudadesViewModel) -> Unit = {}
) {
    val navController = rememberNavController()
    var ciudadSeleccionada: Ciudad? = null

    NavHost(
        navController = navController,
        startDestination = "ciudades"
    ) {
        composable("ciudades") {
            CiudadesPage(
                onCiudadSeleccionada = { ciudad ->
                    ciudadSeleccionada = ciudad
                    navController.navigate("clima")
                },
                onBuscarUbicacion = onBuscarUbicacion
            )
        }

        composable("clima") {
            ciudadSeleccionada?.let { ciudad ->
                ClimaPage(
                    ciudad = ciudad,
                    onVolverACiudades = {
                        navController.popBackStack()
                    },
                    onCompartir = onCompartir
                )
            }
        }
    }
}