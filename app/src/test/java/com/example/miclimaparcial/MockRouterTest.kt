package com.example.miclimaparcial

import com.example.miclimaparcial.router.MockRouter
import com.example.miclimaparcial.router.Ruta
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class MockRouterTest {

    @Test
    fun `navigate to Ciudades logs correctly`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val mockRouter = MockRouter()
        mockRouter.navegar(Ruta.Ciudades)

        assert(outputStream.toString().contains("Navegar a: ciudades"))
    }

    @Test
    fun `navigate to ClimaFloat logs correctly`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val mockRouter = MockRouter()
        val ruta = Ruta.ClimaFloat(lat = 10.0f, lon = 20.0f, nombre = "TestCity")
        mockRouter.navegar(ruta)

        assert(outputStream.toString().contains("Navegar a: clima_float?lat=10.0&lon=20.0&nombre=TestCity"))
    }

    @Test
    fun `navigate to ClimaDouble logs correctly`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val mockRouter = MockRouter()
        val ruta = Ruta.ClimaDouble(lat = 10.0, lon = 20.0, nombre = "TestCity")
        mockRouter.navegar(ruta)

        assert(outputStream.toString().contains("Navegar a: clima_double?lat=10.0&lon=20.0&nombre=TestCity"))
    }
}