package com.example.pasteleriamilsabores.model

import androidx.annotation.DrawableRes

data class Product(
    val id: Int? = null,
    val name: String,
    val price: Double,
    val stock: Int,
    val imageUrl: String? = null
)