package com.example.pasteleriamilsabores.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem(AppScreens.HomeScreen.route, Icons.Default.Home, "Inicio")
    object Products : BottomNavItem(AppScreens.ProductsScreen.route, Icons.Default.ShoppingCart, "Productos")
    object Blog : BottomNavItem(AppScreens.BlogScreen.route, Icons.Default.Book, "Blog")
    object Register : BottomNavItem(AppScreens.RegistroScreen.route, Icons.Default.AccountCircle, "Registro")
}