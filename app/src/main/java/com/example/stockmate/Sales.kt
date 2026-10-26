package com.example.stockmate

data class Sale(
    val id: String = "",
    val items: List<BillItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val totalItems: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)

data class BillItem(
    val productId: String = "",
    val productName: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0,
    val totalPrice: Double = 0.0
)