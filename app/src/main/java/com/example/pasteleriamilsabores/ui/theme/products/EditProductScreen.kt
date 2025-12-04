package com.example.pasteleriamilsabores.ui.theme.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pasteleriamilsabores.viewModel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    navController: NavController,
    productoViewModel: ProductoViewModel = viewModel(),
    productId: Int?
) {
    // Buscar el producto por ID
    val product = productoViewModel.products.value.find { it.id == productId }

    // Estados del formulario
    var name by remember { mutableStateOf(product?.name ?: "") }
    var price by remember { mutableStateOf(product?.price?.toString() ?: "") }
    var stock by remember { mutableStateOf(product?.stock?.toString() ?: "") }
    var imageUrl by remember { mutableStateOf(product?.imageUrl ?: "") }

    // Estados de error
    var nameError by remember { mutableStateOf(false) }
    var priceError by remember { mutableStateOf(false) }
    var stockError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campo Nombre
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = it.isBlank()
                },
                label = { Text("Nombre del producto *") },
                isError = nameError,
                supportingText = {
                    if (nameError) {
                        Text("El nombre es obligatorio")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo Precio
            OutlinedTextField(
                value = price,
                onValueChange = {
                    price = it
                    val priceValue = it.toDoubleOrNull()
                    priceError = priceValue == null || priceValue <= 0
                },
                label = { Text("Precio *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = priceError,
                supportingText = {
                    if (priceError) {
                        Text("Precio debe ser mayor a 0")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo Stock
            OutlinedTextField(
                value = stock,
                onValueChange = {
                    stock = it
                    val stockValue = it.toIntOrNull()
                    stockError = stockValue == null || stockValue < 0
                },
                label = { Text("Stock *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = stockError,
                supportingText = {
                    if (stockError) {
                        Text("Stock no puede ser negativo")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo URL de imagen (opcional)
            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("URL de la imagen (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    // Resetear errores
                    nameError = name.isBlank()
                    val priceValue = price.toDoubleOrNull()
                    priceError = priceValue == null || priceValue <= 0
                    val stockValue = stock.toIntOrNull()
                    stockError = stockValue == null || stockValue < 0

                    // Si no hay errores y el ID existe, actualizar
                    if (!nameError && !priceError && !stockError && productId != null) {
                        val updatedProduct = com.example.pasteleriamilsabores.model.Product(
                            id = productId,
                            name = name,
                            price = priceValue!!,
                            stock = stockValue!!,
                            imageUrl = imageUrl.ifBlank { null }
                        )
                        productoViewModel.updateProduct(productId, updatedProduct)
                        navController.navigateUp()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Actualizar Producto")
            }

            // Nota: campos obligatorios
            Text(
                text = "* Campos obligatorios",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}