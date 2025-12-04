package com.example.pasteleriamilsabores.data.repository

import com.example.pasteleriamilsabores.data.remote.WeatherApi
import com.example.pasteleriamilsabores.data.remote.WeatherService
import com.example.pasteleriamilsabores.model.Main
import com.example.pasteleriamilsabores.model.Weather
import com.example.pasteleriamilsabores.model.WeatherResponse
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkObject

class WeatherRepositoryTest : FunSpec({

    lateinit var repository: WeatherRepository
    val mockService: WeatherService = mockk()

    beforeSpec {
        mockkObject(WeatherApi)
        coEvery { WeatherApi.api } returns mockService
        repository = WeatherRepository()
    }

    test("getWeather success") {
        val expectedResponse = WeatherResponse(
            main = Main(temp = 18.0, humidity = 70),
            weather = listOf(Weather(main = "Clouds", description = "nubes dispersas"))
        )
        coEvery { mockService.getWeather("Santiago", "fakeKey", "metric", "es") } returns expectedResponse

        val result = repository.getWeather("Santiago", "fakeKey")

        result shouldBe expectedResponse
    }

    test("getWeather failure") {
        val errorMessage = "API key invalid"
        coEvery { mockService.getWeather("Santiago", "invalidKey", any(), any()) } throws RuntimeException(errorMessage)

        try {
            repository.getWeather("Santiago", "invalidKey")
        } catch (e: RuntimeException) {
            e.message shouldBe errorMessage
        }
    }
})