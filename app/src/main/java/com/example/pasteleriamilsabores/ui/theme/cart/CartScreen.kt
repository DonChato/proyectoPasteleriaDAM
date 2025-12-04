package com.example.pasteleriamilsabores.ui.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pasteleriamilsabores.model.CartItem
import com.example.pasteleriamilsabores.viewModel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel
) {
    val uiState by cartViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito de Compras") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues).fillMaxSize()) {
            if (uiState.items.isEmpty()) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text("Tu carrito está vacío")
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(uiState.items) { item -> // Ahora son CartItem
                        CartItemRow(item = item, onRemove = { cartViewModel.removeProduct(it) })
                        Divider()
                    }
                }
                Text(
                    text = "Total: $${"%.2f".format(uiState.totalPrice)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp).align(Alignment.End)
                )
            }
        }
    }
}
@Composable
fun CartItemRow(item: CartItem, onRemove: (CartItem) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(item.name, fontWeight = FontWeight.Bold)
            Text("$${item.price}")
        }
        IconButton(onClick = { onRemove(item) }) {
            Icon(Icons.Default.Delete, contentDescription = "Eliminar producto")
        }
    }
}