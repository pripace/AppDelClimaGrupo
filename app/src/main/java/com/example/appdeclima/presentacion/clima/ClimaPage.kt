package com.example.appdeclima.presentacion.clima

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appdeclima.repository.modelos.Ciudad

fun getEmojiForWeather(mainCondition: String): String {
    val condition = mainCondition.lowercase().trim()

    return when {
        condition.contains("claro") ||
                condition.contains("despejado") -> "☀️"
        condition.contains("nube") ||
                condition.contains("nuboso") -> {

            when {
                condition.contains("algo") ||
                        condition.contains("pocas") -> "⛅"
                condition.contains("muy") ||
                        condition.contains("cubierto") -> "☁️"
                else -> "☁️"
            }
        }
        condition.contains("lluvia") -> {
            when {
                condition.contains("ligera") -> "🌦️"
                condition.contains("fuerte") ||
                        condition.contains("intensa") -> "🌧️"
                else -> "🌧️"
            }
        }
        condition.contains("llovizna") -> "🌦️"
        condition.contains("nieve") ||
                condition.contains("nevada") -> {
            when {
                condition.contains("ligera") -> "🌨️"
                condition.contains("fuerte") ||
                        condition.contains("intensa") -> "❄️"
                else -> "❄️"
            }
        }
        condition.contains("tormenta") ||
                condition.contains("trueno") -> "⛈️"
        condition.contains("niebla") ||
                condition.contains("neblina") ||
                condition.contains("bruma") -> "🌫️"
        condition.contains("tornado") ||
                condition.contains("ráfaga") -> "🌪️"
        else -> "❔"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClimaPage(
    ciudad: Ciudad,
    viewModel: ClimaViewModel = viewModel(),
    onVolverACiudades: () -> Unit = {},
    onCompartir: (String) -> Unit = {}
) {
    LaunchedEffect(ciudad) {
        viewModel.manejarIntencion(IntencionClima.CargarClima(ciudad))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Clima",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onVolverACiudades() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        when (val estado = viewModel.estado) {
                            is EstadoClima.Exito -> {
                                val textoCompartir = "El clima en ${estado.clima.ciudad.nombre} es ${estado.clima.temperatura}°C, ${estado.clima.descripcion}"
                                onCompartir(textoCompartir)
                            }
                            else -> {}
                        }
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Compartir")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            when (val estado = viewModel.estado) {
                is EstadoClima.Exito -> {
                    Text(
                        text = "${estado.clima.temperatura}°C",
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = estado.clima.descripcion.replaceFirstChar { it.uppercase() },
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Pronóstico 7 días",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (estado.pronostico.size >= 2) {
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        ) {
                            val maxTemp = estado.pronostico.maxOf { it.temperaturaMax }
                            val minTemp = estado.pronostico.minOf { it.temperaturaMin }
                            val tempRange = maxTemp - minTemp
                            val barWidth = size.width / (estado.pronostico.size * 1.5f)
                            val gap = barWidth / 2

                            estado.pronostico.forEachIndexed { index, dia ->
                                val x = index * (barWidth + gap) + barWidth / 2
                                val barHeight = (dia.temperaturaMax - minTemp).toFloat() / tempRange * size.height * 0.6f
                                val y = size.height * 0.8f - barHeight

                                drawRoundRect(
                                    color = Color(0xFF90CAF9),
                                    topLeft = Offset(x, y),
                                    size = androidx.compose.ui.geometry.Size(barWidth, barHeight),
                                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx())
                                )

                                drawContext.canvas.nativeCanvas.drawText(
                                    "${dia.temperaturaMax}°",
                                    x + barWidth / 2,
                                    y - 8.dp.toPx(),
                                    android.graphics.Paint().apply {
                                        textAlign = android.graphics.Paint.Align.CENTER
                                        textSize = 28f
                                        color = android.graphics.Color.WHITE
                                        isAntiAlias = true
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    estado.pronostico.forEach { dia ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = dia.fecha, fontSize = 14.sp)

                            Text(
                                text = getEmojiForWeather(dia.descripcion),
                                fontSize = 24.sp,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )

                            Text(
                                text = "${dia.temperaturaMax}°/${dia.temperaturaMin}°",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                is EstadoClima.Cargando -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is EstadoClima.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = estado.mensaje, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}






