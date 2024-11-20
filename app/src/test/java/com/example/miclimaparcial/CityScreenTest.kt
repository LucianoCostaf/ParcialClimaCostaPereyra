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
class CityScreenTest {

    private lateinit var mockService: RetrofitClient

    @Before
    fun setUp() {
        mockService = mockk(relaxed = true)
    }

    @Test
    fun `searchCities successful with results`() = runTest {
        val fakeCities = listOf(
            City(name = "Buenos Aires", coord = Coord(-34.6037f, -58.3816f)),
            City(name = "La Plata", coord = Coord(-34.9214f, -57.9545f))
        )
        coEvery { mockService.weatherServiceV2.searchCities("Buenos", any()) } returns CitySearchResponse(list = fakeCities)

        val result = mockService.weatherServiceV2.searchCities("Buenos", "fakeApiKey")

        assertEquals(2, result.list.size)
        assertEquals("Buenos Aires", result.list[0].name)
    }

    @Test
    fun `searchCities with empty query does not call API`() = runTest {
        val emptyQuery = ""
        coEvery { mockService.weatherServiceV2.searchCities(emptyQuery, any()) } returns CitySearchResponse(list = listOf())

        val result = mockService.weatherServiceV2.searchCities(emptyQuery, "fakeApiKey")

        assertEquals(0, result.list.size)
    }

    @Test
    fun `searchCities handles API error gracefully`() = runTest {
        coEvery { mockService.weatherServiceV2.searchCities(any(), any()) } throws Exception("Error de red")

        try {
            mockService.weatherServiceV2.searchCities("Buenos", "fakeApiKey")
            assert(false)
        } catch (e: Exception) {
            assertEquals("Error de red", e.message)
        }
    }
}
