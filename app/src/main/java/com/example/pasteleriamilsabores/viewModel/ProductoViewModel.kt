package com.example.pasteleriamilsabores.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores.data.remote.RetrofitInstance
import com.example.pasteleriamilsabores.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class ProductoViewModel : ViewModel() {

    private val api = RetrofitInstance.api

    // Estado de la lista de productos
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Estado de carga
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    // Estado para operaciones exitosas
    private val _operationSuccess = MutableStateFlow(false)
    val operationSuccess: StateFlow<Boolean> = _operationSuccess.asStateFlow()

    // Cargar todos los productos
    fun loadProducts() {
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val productList = api.getProducts()
                _products.value = productList
            } catch (e: Exception) {
                _error.value = "Error al cargar productos: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    // Crear un nuevo producto
    fun createProduct(product: Product) {
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val createdProduct = api.createProduct(product)
                _products.value = _products.value + createdProduct
                _operationSuccess.value = true
            } catch (e: Exception) {
                _error.value = "Error al crear producto: ${e.message}"
                _operationSuccess.value = false
            } finally {
                _loading.value = false
            }
        }
    }

    // Actualizar un producto existente
    fun updateProduct(id: Int, product: Product) {
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val updatedProduct = api.updateProduct(id, product)
                _products.value = _products.value.map { if (it.id == id) updatedProduct else it }
                _operationSuccess.value = true
            } catch (e: Exception) {
                _error.value = "Error al actualizar producto: ${e.message}"
                _operationSuccess.value = false
            } finally {
                _loading.value = false
            }
        }
    }

    // Eliminar un producto
    fun deleteProduct(id: Int) {
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val response: Response<Unit> = api.deleteProduct(id)
                if (response.isSuccessful) {
                    _products.value = _products.value.filter { it.id != id }
                    _operationSuccess.value = true
                } else {
                    _error.value = "Error al eliminar: ${response.code()}"
                    _operationSuccess.value = false
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.message}"
                _operationSuccess.value = false
            } finally {
                _loading.value = false
            }
        }
    }

    // Limpiar errores
    fun clearError() {
        _error.value = null
    }

    // Limpiar estado de operación exitosa
    fun clearOperationSuccess() {
        _operationSuccess.value = false
    }
}