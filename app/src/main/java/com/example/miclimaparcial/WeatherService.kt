package com.example.miclimaparcial.api

import retrofit2.http.GET
import retrofit2.http.Query

// Interfaz API 2.5
interface WeatherServiceV2 {

    @GET("weather")
    suspend fun getWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es"
    ): WeatherResponse

    @GET("find")
    suspend fun searchCities(
        @Query("q") query: String,
        @Query("appid") apiKey: String
    ): CitySearchResponse

    @GET("weather")
    suspend fun searchWeatherByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es"
    ): WeatherResponse

}

// Interfaz API 3.0
interface WeatherServiceV3 {

    @GET("onecall")
    suspend fun getSevenDayForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es"
    ): WeeklyForecastResponse
}

