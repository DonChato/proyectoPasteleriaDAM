package com.example.pasteleriamilsabores.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores.db.AppDatabase
import com.example.pasteleriamilsabores.model.CartItem
import com.example.pasteleriamilsabores.model.Product
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val totalPrice: Double = 0.0
)
class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val cartDao = AppDatabase.getDatabase(application).cartDao()

    val uiState: StateFlow<CartUiState> = cartDao.getAllItems()
        .map { itemList ->
            CartUiState(
                items = itemList,
                totalPrice = itemList.sumOf { it.price }
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = CartUiState()
        )

    fun addProduct(product: Product) {
        viewModelScope.launch {
            val cartItem = CartItem(
                productId = product.id ?: 0, // Si id es nullable
                name = product.name,
                price = product.price
            )
            cartDao.insertItem(cartItem)
        }
    }

    fun removeProduct(cartItem: CartItem) {
        viewModelScope.launch {
            cartDao.deleteItem(cartItem)
        }
    }
}