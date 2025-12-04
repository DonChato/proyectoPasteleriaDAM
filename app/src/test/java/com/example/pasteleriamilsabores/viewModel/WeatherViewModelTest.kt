package com.example.pasteleriamilsabores.viewModel

import com.example.pasteleriamilsabores.data.repository.WeatherRepository
import com.example.pasteleriamilsabores.model.Main
import com.example.pasteleriamilsabores.model.Weather
import com.example.pasteleriamilsabores.model.WeatherResponse
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@ExperimentalCoroutinesApi
class WeatherViewModelTest : FunSpec({

    lateinit var viewModel: WeatherViewModel
    val mockRepository: WeatherRepository = mockk()
    val testDispatcher = UnconfinedTestDispatcher()

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
        viewModel = WeatherViewModel(mockRepository)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    test("loadWeather success") {
        val fakeWeather = WeatherResponse(
            main = Main(temp = 22.5, humidity = 65),
            weather = listOf(Weather(main = "Clear", description = "cielo despejado"))
        )
        coEvery { mockRepository.getWeather("Santiago", "fakeApiKey") } returns fakeWeather

        viewModel.loadWeather("Santiago", "fakeApiKey")

        delay(100)
        val state = viewModel.uiState
        state shouldBe WeatherUiState.Success(fakeWeather)
    }

    test("loadWeather failure") {
        val errorMessage = "City not found"
        coEvery { mockRepository.getWeather("InvalidCity", "fakeApiKey") } throws RuntimeException(errorMessage)

        viewModel.loadWeather("InvalidCity", "fakeApiKey")

        delay(100)
        val state = viewModel.uiState
        state shouldBe WeatherUiState.Error(errorMessage)
    }
})