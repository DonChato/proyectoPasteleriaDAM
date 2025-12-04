package com.example.pasteleriamilsabores.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores.data.repository.WeatherRepository
import kotlinx.coroutines.launch

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val weather: com.example.pasteleriamilsabores.model.WeatherResponse) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

class WeatherViewModel(
    private val repository: WeatherRepository = WeatherRepository()
) : ViewModel() {

    var uiState: WeatherUiState by mutableStateOf(WeatherUiState.Loading)
        private set

    fun loadWeather(city: String, apiKey: String) {
        uiState = WeatherUiState.Loading
        viewModelScope.launch {
            try {
                val weather = repository.getWeather(city, apiKey)
                uiState = WeatherUiState.Success(weather)
            } catch (e: Exception) {
                uiState = WeatherUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}