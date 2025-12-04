package com.example.pasteleriamilsabores.model

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>
)

data class Main(
    val temp: Double,
    val humidity: Int,
    val temp_min: Double? = null,
    val temp_max: Double? = null
)

data class Weather(
    val main: String,
    val description: String
)