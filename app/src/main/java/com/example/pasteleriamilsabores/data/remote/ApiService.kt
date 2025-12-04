package com.example.pasteleriamilsabores.data.remote

import com.example.pasteleriamilsabores.model.Product
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("api/v1/productos")
    suspend fun getProducts(): List<Product>

    @POST("api/v1/productos")
    suspend fun createProduct(@Body product: Product): Product

    @PUT("api/v1/productos/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body product: Product): Product

    @DELETE("api/v1/productos/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<Unit>
}