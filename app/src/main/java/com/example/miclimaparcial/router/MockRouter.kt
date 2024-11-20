package com.example.miclimaparcial.router

class MockRouter : Router {
    override fun navegar(ruta: Ruta) {
        when (ruta) {

            Ruta.Ciudades -> println("Navegar a: ${ruta.id}")

            //clima (API 2.5)
            is Ruta.ClimaFloat -> {
                println("Navegar a: ${ruta.id}?lat=${ruta.lat}&lon=${ruta.lon}&nombre=${ruta.nombre}")
            }

            // clima (API 3.0)
            is Ruta.ClimaDouble -> {
                println("Navegar a: ${ruta.id}?lat=${ruta.lat}&lon=${ruta.lon}&nombre=${ruta.nombre}")
            }
        }
    }
}
