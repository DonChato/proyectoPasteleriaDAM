package com.example.pasteleriamilsabores.ui.products

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pasteleriamilsabores.navigation.AppScreens
import com.example.pasteleriamilsabores.ui.theme.products.ProductCard
import com.example.pasteleriamilsabores.viewModel.CartViewModel
import com.example.pasteleriamilsabores.viewModel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    navController: NavController,
    cartViewModel: CartViewModel = viewModel(),
    productoViewModel: ProductoViewModel = viewModel()
) {
    // Estados del ViewModel
    val products by productoViewModel.products.collectAsState()
    val loading by productoViewModel.loading.collectAsState()
    val error by productoViewModel.error.collectAsState()

    // Cargar productos al iniciar
    LaunchedEffect(Unit) {
        productoViewModel.loadProducts()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Nuestro Catálogo") }) },
        floatingActionButton = {
            Row {
                // Botón para agregar producto
                FloatingActionButton(
                    onClick = {
                        // Navegar a pantalla de creación
                        navController.navigate("add_product")
                    },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar Producto")
                }
                // Botón del carrito
                FloatingActionButton(
                    onClick = { navController.navigate(AppScreens.CartScreen.route) }
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Ver Carrito")
                }
            }
        }
    ) { innerPadding ->
        when {
            loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(innerPadding))
            }
            error != null -> {
                Text(
                    text = "Error: $error",
                    modifier = Modifier.padding(innerPadding)
                )
            }
            products.isEmpty() -> {
                Text(
                    text = "No hay productos disponibles",
                    modifier = Modifier.padding(innerPadding)
                )
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.padding(innerPadding)
                ) {
                    items(products) { product ->
                        ProductCard(
                            product = product,
                            onAddToCart = { cartViewModel.addProduct(it) },
                            onEdit = { productToEdit ->
                                // Navegar a pantalla de edición (crearemos luego)
                                navController.navigate("edit_product/${productToEdit.id}")
                            },
                            onDelete = { productToDelete ->
                                // Llamar al ViewModel para eliminar
                                productoViewModel.deleteProduct(productToDelete.id ?: 0)
                            }
                        )
                    }
                }
            }
        }
    }
}