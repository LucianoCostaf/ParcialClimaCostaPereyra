package com.example.miclimaparcial

import com.example.miclimaparcial.api.RetrofitClient
import org.junit.Assert.assertNotNull
import org.junit.Test

class RetrofitClientTest {

    @Test
    fun `weatherServiceV2 is initialized`() {
        assertNotNull(RetrofitClient.weatherServiceV2)
    }

    @Test
    fun `weatherServiceV3 is initialized`() {
        assertNotNull(RetrofitClient.weatherServiceV3)
    }
}