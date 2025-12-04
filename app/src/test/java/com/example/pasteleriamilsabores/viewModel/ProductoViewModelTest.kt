package com.example.pasteleriamilsabores.viewModel

import com.example.pasteleriamilsabores.data.remote.ApiService
import com.example.pasteleriamilsabores.data.remote.RetrofitInstance
import com.example.pasteleriamilsabores.model.Product
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import retrofit2.Response

@ExperimentalCoroutinesApi
class ProductoViewModelTest : FunSpec({

    lateinit var viewModel: ProductoViewModel
    val mockApi: ApiService = mockk()
    val testDispatcher = UnconfinedTestDispatcher()

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
        mockkObject(RetrofitInstance)
        coEvery { RetrofitInstance.api } returns mockApi
        viewModel = ProductoViewModel()
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    test("loadProducts success") {
        val fakeProducts = listOf(
            Product(id = 1, name = "Torta", price = 100.0, stock = 10),
            Product(id = 2, name = "Pastel", price = 200.0, stock = 5)
        )
        coEvery { mockApi.getProducts() } returns fakeProducts

        viewModel.loadProducts()

        delay(100)
        viewModel.products.value shouldBe fakeProducts
        viewModel.error.value shouldBe null
    }

    test("loadProducts failure") {
        val errorMessage = "Network error"
        coEvery { mockApi.getProducts() } throws RuntimeException(errorMessage)

        viewModel.loadProducts()

        delay(100)
        viewModel.error.value shouldBe "Error al cargar productos: $errorMessage"
    }

    test("createProduct success") {
        val newProduct = Product(name = "Nuevo", price = 50.0, stock = 20)
        val createdProduct = newProduct.copy(id = 3)
        coEvery { mockApi.createProduct(newProduct) } returns createdProduct

        viewModel.createProduct(newProduct)

        delay(100)
        viewModel.products.value shouldContain createdProduct
        viewModel.operationSuccess.value shouldBe true
    }

    test("createProduct failure") {
        val newProduct = Product(name = "Nuevo", price = 50.0, stock = 20)
        val errorMessage = "Creation failed"
        coEvery { mockApi.createProduct(newProduct) } throws RuntimeException(errorMessage)

        viewModel.createProduct(newProduct)

        delay(100)
        viewModel.error.value shouldBe "Error al crear producto: $errorMessage"
        viewModel.operationSuccess.value shouldBe false
    }

    test("updateProduct success") {
        val existingProduct = Product(id = 1, name = "Viejo", price = 100.0, stock = 10)
        val updatedProduct = existingProduct.copy(name = "Actualizado")
        coEvery { mockApi.updateProduct(1, updatedProduct) } returns updatedProduct

        viewModel.loadProducts()
        viewModel.updateProduct(1, updatedProduct)

        delay(100)
        viewModel.operationSuccess.value shouldBe true
    }

    test("updateProduct failure") {
        val product = Product(id = 1, name = "Test", price = 50.0, stock = 10)
        coEvery { mockApi.updateProduct(1, product) } throws RuntimeException("Update failed")

        viewModel.updateProduct(1, product)

        delay(100)
        viewModel.error.value shouldBe "Error al actualizar producto: Update failed"
        viewModel.operationSuccess.value shouldBe false
    }

    test("deleteProduct success") {
        coEvery { mockApi.deleteProduct(1) } returns Response.success(Unit)

        viewModel.deleteProduct(1)

        delay(100)
        viewModel.operationSuccess.value shouldBe true
    }

    test("deleteProduct failure") {
        val responseBody: ResponseBody = mockk {
            coEvery { contentType() } returns null
            coEvery { contentLength() } returns 0L
        }
        val errorResponse = Response.error<Unit>(404, responseBody)

        coEvery { mockApi.deleteProduct(1) } returns errorResponse

        viewModel.deleteProduct(1)

        delay(100)
        viewModel.error.value shouldBe "Error al eliminar: 404"
        viewModel.operationSuccess.value shouldBe false
    }
})