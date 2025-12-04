package com.example.pasteleriamilsabores.data.repository

import com.example.pasteleriamilsabores.data.remote.WeatherApi
import com.example.pasteleriamilsabores.model.WeatherResponse

class WeatherRepository {
    suspend fun getWeather(city: String, apiKey: String): WeatherResponse {
        return WeatherApi.api.getWeather(city, apiKey)
    }
}