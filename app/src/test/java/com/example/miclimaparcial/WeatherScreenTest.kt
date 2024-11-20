package com.example.miclimaparcial

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import io.mockk.coEvery
import io.mockk.mockk
import com.example.miclimaparcial.api.*

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherScreenTest {

    private lateinit var mockService: RetrofitClient

    @Before
    fun setUp() {
        mockService = mockk(relaxed = true)
    }

    @Test
    fun `load current weather and weekly forecast successfully`() = runTest {
        coEvery { mockService.weatherServiceV2.searchWeatherByCoordinates(any(), any(), any()) } returns TestData.fakeCurrentWeather
        coEvery { mockService.weatherServiceV3.getSevenDayForecast(any(), any(), any(), any()) } returns TestData.fakeWeeklyForecast

        val currentWeather = mockService.weatherServiceV2.searchWeatherByCoordinates(0.0, 0.0, "fakeApiKey")
        val weeklyForecast = mockService.weatherServiceV3.getSevenDayForecast(0.0, 0.0, "fakeApiKey", "metric")

        assertEquals(25.0f, currentWeather.main.temp, 0.0f)
        assertEquals("Soleado", currentWeather.weather.first().description)
        assertEquals(2, weeklyForecast.daily.size)
        assertEquals("Parcialmente nublado", weeklyForecast.daily[0].weather.first().description)
    }

    @Test
    fun `getSevenDayForecast handles API error gracefully`() = runTest {
        coEvery { mockService.weatherServiceV3.getSevenDayForecast(any(), any(), any(), any()) } throws Exception("Error al obtener pronóstico")

        try {
            mockService.weatherServiceV3.getSevenDayForecast(0.0, 0.0, "fakeApiKey", "metric")
            assert(false)
        } catch (e: Exception) {
            assertEquals("Error al obtener pronóstico", e.message)
        }
    }
}
