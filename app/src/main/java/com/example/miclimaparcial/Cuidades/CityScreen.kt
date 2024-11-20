package com.example.miclimaparcial.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.miclimaparcial.api.City
import com.example.miclimaparcial.api.RetrofitClient
import com.example.miclimaparcial.router.Router
import com.example.miclimaparcial.router.Ruta
import kotlinx.coroutines.launch
import com.example.miclimaparcial.api.Coord
import com.example.miclimaparcial.ApiConfig

@Composable
fun CityScreen(router: Router, latitude: Double?, longitude: Double?) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var cities by remember { mutableStateOf<List<City>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showNoResultsError by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    // Ciudades populares
    val popularCities = listOf(
        City(name = "Rabat", coord = Coord(34.0209f, -6.8417f)),
        City(name = "Londres", coord = Coord(51.5074f, -0.1278f)),
        City(name = "Quito", coord = Coord(-0.1807f, -78.4678f)),
        City(name = "Buenos Aires", coord = Coord(-34.6037f, -58.3816f)),
        City(name = "Sídney", coord = Coord(-33.8688f, 151.2093f))
    )

    fun searchCities(query: String, showError: Boolean = false) {
        if (query.isNotEmpty()) {
            isLoading = true
            errorMessage = null
            coroutineScope.launch {
                try {
                    val response = RetrofitClient.weatherServiceV2.searchCities(query, ApiConfig.apiKey)
                    cities = response.list ?: emptyList()
                    showNoResultsError = showError && cities.isEmpty()
                    errorMessage = if (showNoResultsError) "Ciudad no encontrada." else null
                } catch (e: Exception) {
                    errorMessage = if (showError) "Error al buscar las ciudades." else null
                } finally {
                    isLoading = false
                }
            }
        } else {
            cities = emptyList()
            errorMessage = null
            showNoResultsError = false
        }
    }

    fun searchCitiesByCoordinates(lat: Double, lon: Double) {
        isLoading = true
        coroutineScope.launch {
            try {
                val response = RetrofitClient.weatherServiceV2.searchWeatherByCoordinates(lat, lon, ApiConfig.apiKey)
                cities = listOf(City(name = response.name, coord = Coord(lat = lat.toFloat(), lon = lon.toFloat())))
                errorMessage = null
            } catch (e: Exception) {
                errorMessage = "Error al buscar la ubicación."
            } finally {
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                searchCities(searchQuery.text)
                showNoResultsError = false
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            placeholder = { Text(text = "Escriba aquí su ciudad...") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { searchCities(searchQuery.text, showError = true) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Buscar Ciudades")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    latitude?.let { lat ->
                        longitude?.let { lon ->
                            searchCitiesByCoordinates(lat, lon)
                        } ?: run { errorMessage = "Coordenadas no disponibles." }
                    } ?: run { errorMessage = "Coordenadas no disponibles." }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Ubicación Actual")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        errorMessage?.let {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFCDD2)),
                modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth()
            ) {
                Text(
                    text = it,
                    color = Color(0xFFD32F2F),
                    modifier = Modifier.padding(16.dp),
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                )
            }
        }

        if (isLoading) {
            Text("Cargando...")
        } else {
            LazyColumn {
                if (cities.isNotEmpty()) {
                    item {
                        Text("Resultados de Búsqueda", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp))
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    items(cities) { city ->
                        Card(
                            modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth().clickable {
                                router.navegar(Ruta.ClimaDouble(city.coord.lat.toDouble(), city.coord.lon.toDouble(), city.name))
                            },
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Text(text = city.name, modifier = Modifier.padding(16.dp), style = TextStyle(fontSize = 16.sp))
                        }
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }

                item {
                    Text("Ciudades Populares", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp))
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(popularCities) { city ->
                    Card(
                        modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth().clickable {
                            router.navegar(Ruta.ClimaDouble(city.coord.lat.toDouble(), city.coord.lon.toDouble(), city.name))
                        },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Text(text = city.name, modifier = Modifier.padding(16.dp), style = TextStyle(fontSize = 16.sp))
                    }
                }
            }
        }
    }
}



