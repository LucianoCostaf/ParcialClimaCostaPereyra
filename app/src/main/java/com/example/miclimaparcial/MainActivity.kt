package com.example.miclimaparcial

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.miclimaparcial.Clima.WeatherScreen
import com.example.miclimaparcial.ui.theme.MiClimaParcialTheme
import com.example.miclimaparcial.router.Enrutador
import com.example.miclimaparcial.router.Router
import com.example.miclimaparcial.router.Ruta
import com.example.miclimaparcial.screens.CityScreen
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private lateinit var router: Router
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val REQUEST_LOCATION_PERMISSION = 1
    var errorMessage: String? = null
    var currentLatitude: Double? = null
    var currentLongitude: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getCurrentLocation { lat, lon ->
            currentLatitude = lat
            currentLongitude = lon

            initUI()
        }
    }
    private fun initUI() {
        val favoriteCity = UserPreferences.getFavoriteCity(this)

        setContent {
            MiClimaParcialTheme {
                navController = rememberNavController()
                router = Enrutador(navController)

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = if (favoriteCity != null && currentLatitude != null && currentLongitude != null) {
                            "clima_double?lat=${currentLatitude}&lon=${currentLongitude}&nombre=$favoriteCity"
                        } else {
                            Ruta.Ciudades.id
                        },
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Ruta.Ciudades.id) {
                            CityScreen(router, currentLatitude, currentLongitude)
                        }

                        //ClimaFloat(API 2.5)
                        composable(
                            "clima_float?lat={lat}&lon={lon}&nombre={nombre}",
                            arguments = listOf(
                                navArgument("lat") { type = NavType.FloatType },
                                navArgument("lon") { type = NavType.FloatType },
                                navArgument("nombre") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val lat = backStackEntry.arguments?.getFloat("lat") ?: 0f
                            val lon = backStackEntry.arguments?.getFloat("lon") ?: 0f
                            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
                            WeatherScreen(lat.toDouble(), lon.toDouble(), nombre, router)
                        }
                        //ClimaDouble(API 3.0)
                        composable(
                            "clima_double?lat={lat}&lon={lon}&nombre={nombre}",
                            arguments = listOf(
                                navArgument("lat") { type = NavType.StringType },
                                navArgument("lon") { type = NavType.StringType },
                                navArgument("nombre") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val lat = backStackEntry.arguments?.getString("lat")?.toDoubleOrNull() ?: 0.0
                            val lon = backStackEntry.arguments?.getString("lon")?.toDoubleOrNull() ?: 0.0
                            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
                            WeatherScreen(lat, lon, nombre, router)
                        }
                    }
                }
            }
        }
    }
    private fun getCurrentLocation(callback: (Double?, Double?) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    callback(location.latitude, location.longitude)
                } else {
                    errorMessage = "No se pudo obtener la ubicación."
                    callback(null, null)
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION
            )
            callback(null, null)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation { lat, lon ->
                    currentLatitude = lat
                    currentLongitude = lon
                    initUI()
                }
            } else {
                errorMessage = "Permiso de ubicación no concedido"
            }
        }
    }
}


