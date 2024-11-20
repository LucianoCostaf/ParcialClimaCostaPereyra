package com.example.miclimaparcial.router

import androidx.navigation.NavHostController

interface Router {
    fun navegar(ruta: Ruta)
}

class Enrutador(
    private val navHostController: NavHostController
) : Router {
    override fun navegar(ruta: Ruta) {
        when (ruta) {

            Ruta.Ciudades -> navHostController.navigate(ruta.id)

            // Navegar clima (API 2.5)
            is Ruta.ClimaFloat -> {
                val route = "${ruta.id}?lat=${ruta.lat}&lon=${ruta.lon}&nombre=${ruta.nombre}"
                navHostController.navigate(route)
            }

            // Navegar clima (API 3.0)
            is Ruta.ClimaDouble -> {
                val route = "${ruta.id}?lat=${ruta.lat}&lon=${ruta.lon}&nombre=${ruta.nombre}"
                navHostController.navigate(route)
            }
        }
    }
}

