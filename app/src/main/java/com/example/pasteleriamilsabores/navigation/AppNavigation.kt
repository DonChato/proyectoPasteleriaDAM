package com.example.pasteleriamilsabores.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.pasteleriamilsabores.ui.blog.BlogScreen
import com.example.pasteleriamilsabores.ui.cart.CartScreen
import com.example.pasteleriamilsabores.ui.home.HomeScreen
import com.example.pasteleriamilsabores.ui.products.ProductsScreen
import com.example.pasteleriamilsabores.ui.registro.RegistroScreen
import com.example.pasteleriamilsabores.ui.registro.ResumenScreen
import com.example.pasteleriamilsabores.viewModel.CartViewModel
import com.example.pasteleriamilsabores.viewModel.RegistroViewModel
import com.example.pasteleriamilsabores.viewModel.ProductoViewModel
import com.example.pasteleriamilsabores.ui.theme.products.AddProductScreen
import com.example.pasteleriamilsabores.ui.theme.products.EditProductScreen


sealed class AppScreens(val route: String) {
    object HomeScreen : AppScreens("home_screen")
    object ProductsScreen : AppScreens("products_screen")
    object BlogScreen : AppScreens("blog_screen")
    object RegistroScreen : AppScreens("registro_screen")
    object CartScreen : AppScreens("cart_screen")
    object ResumenScreen : AppScreens("resumen_screen")


}
@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    val cartViewModel: CartViewModel = viewModel()
    val registroViewModel: RegistroViewModel = viewModel()
    val productoViewModel: ProductoViewModel = viewModel() // ← Agregar

    NavHost(navController, startDestination = AppScreens.HomeScreen.route, modifier = modifier) {
        composable(AppScreens.HomeScreen.route) {
            HomeScreen(navController)
        }
        composable(AppScreens.ProductsScreen.route) {
            ProductsScreen(
                navController = navController,
                cartViewModel = cartViewModel,
                productoViewModel = productoViewModel // ← Pasar al screen
            )
        }
        composable(AppScreens.BlogScreen.route) {
            BlogScreen(navController)
        }
        composable(AppScreens.RegistroScreen.route) {
            RegistroScreen(navController, registroViewModel)
        }
        composable(AppScreens.CartScreen.route) {
            CartScreen(navController, cartViewModel)
        }
        composable(AppScreens.ResumenScreen.route) {
            ResumenScreen(navController, registroViewModel)
        }
        composable("add_product") {
            AddProductScreen(navController)
        }
        composable(
            route = "edit_product/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId")
            EditProductScreen(navController, productId = productId)
        }
    }
}