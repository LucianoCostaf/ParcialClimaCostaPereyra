package com.example.miclimaparcial

import com.example.miclimaparcial.api.*

object TestData {
    val fakeCurrentWeather = WeatherResponse(
        main = Main(
            temp = 25.0f,
            temp_max = 30.0f,
            temp_min = 20.0f,
            pressure = 1013f,
            humidity = 60f
        ),
        weather = listOf(
            Weather(description = "Soleado", icon = "01d")
        ),
        name = "Buenos Aires",
        sys = Sys(country = "AR"),
        wind = Wind(speed = 5.0f)
    )

    val fakeWeeklyForecast = WeeklyForecastResponse(
        lat = -34.6037,
        lon = -58.3816,
        timezone = "America/Argentina/Buenos_Aires",
        timezone_offset = -10800,
        daily = listOf(
            DailyForecast(
                dt = 1697570400,
                sunrise = 1697535600,
                sunset = 1697578800,
                temp = Temp(day = 24.0, max = 30.0, min = 20.0),
                feels_like = FeelsLike(day = 24.0, night = 18.0, eve = 22.0, morn = 20.0),
                pressure = 1015,
                humidity = 50,
                wind_speed = 3.5,
                weather = listOf(
                    Weather(description = "Parcialmente nublado", icon = "03d")
                )
            ),
            DailyForecast(
                dt = 1697656800,
                sunrise = 1697611200,
                sunset = 1697654400,
                temp = Temp(day = 22.0, max = 28.0, min = 18.0),
                feels_like = FeelsLike(day = 22.0, night = 17.0, eve = 20.0, morn = 19.0),
                pressure = 1010,
                humidity = 60,
                wind_speed = 4.0,
                weather = listOf(
                    Weather(description = "Lluvioso", icon = "09d")
                )
            )
        )
    )
}

