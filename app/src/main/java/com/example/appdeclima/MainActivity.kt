package com.example.appdeclima


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.appdeclima.ui.theme.AppDeClimaTheme
import com.example.appdeclima.router.AppNavigation

class MainActivity : ComponentActivity() {

    private var callbackUbicacion: ((Double, Double) -> Unit)? = null

    private val permisoUbicacion = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permisos ->
        if (permisos[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permisos[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            obtenerUbicacion()
        } else {
            callbackUbicacion?.invoke(-34.6118, -58.3960)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppDeClimaTheme {
                AppNavigation(
                    onCompartir = { texto ->
                        compartirTexto(texto)
                    },
                    onBuscarUbicacion = { viewModel ->
                        callbackUbicacion = { lat, lon ->
                            viewModel.manejarCoordenadas(lat, lon)
                        }
                        solicitarUbicacion()
                    }
                )
            }
        }
    }

    private fun compartirTexto(texto: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, texto)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, "Compartir clima"))
    }

    private fun solicitarUbicacion() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                obtenerUbicacion()
            }
            else -> {
                permisoUbicacion.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    private fun obtenerUbicacion() {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        try {
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            if (location != null) {
                callbackUbicacion?.invoke(location.latitude, location.longitude)
            } else {
                callbackUbicacion?.invoke(-34.6118, -58.3960)
            }
        } catch (e: SecurityException) {
            callbackUbicacion?.invoke(-34.6118, -58.3960)
        }
    }
}