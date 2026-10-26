package com.example.stockmate


data class Product(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0,
    val category: String = ""
)