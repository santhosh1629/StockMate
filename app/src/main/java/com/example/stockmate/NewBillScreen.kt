package com.example.stockmate

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewBillScreen(navController: NavHostController) {

    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var searchText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedProducts by remember { mutableStateOf<List<BillItem>>(emptyList()) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var quantityText by remember { mutableStateOf("1") }
    var isSaving by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val productRepository = remember { ProductRepository() }
    val salesRepository = remember { SalesRepository() }

    DisposableEffect(Unit) {
        val registration = productRepository.listenToProducts(
            onProductsChanged = { updatedProducts ->
                products = updatedProducts

                // Refresh selected product's stock
                selectedProduct?.let { selected ->
                    selectedProduct = updatedProducts.find {
                        it.id == selected.id
                    }
                }

                errorMessage = null
            },
            onFailure = { error ->
                errorMessage = error
            }
        )

        onDispose {
            registration.remove()
        }
    }

    val filteredProducts = products.filter {
        it.name.contains(searchText, ignoreCase = true) ||
                it.category.contains(searchText, ignoreCase = true)
    }

    val quantity = quantityText.toIntOrNull() ?: 0
    val grandTotal = selectedProducts.sumOf { it.totalPrice }
    val totalBillItems = selectedProducts.sumOf { it.quantity }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Bill") },
                navigationIcon = {
                    TextButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Text("Back")
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Search products") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Available Products",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "Unable to load products",
                    color = Color.Red
                )
            }

            // Product list
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(
                    items = filteredProducts,
                    key = { it.id }
                ) { product ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor =
                                if (selectedProduct?.id == product.id)
                                    Color(0xFFB2DFDB)
                                else
                                    Color(0xFFE8F5E9)
                        ),
                        onClick = {
                            selectedProduct = product
                            quantityText = "1"
                        }
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = product.name,
                                fontWeight = FontWeight.Bold
                            )

                            Text("Price: ₹${product.price}")
                            Text("Stock: ${product.quantity}")

                            Text(
                                text = if (selectedProduct?.id == product.id)
                                    "Selected"
                                else
                                    "Tap to select",
                                color = Color(0xFF008577),
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                if (filteredProducts.isEmpty() && errorMessage == null) {
                    item {
                        Text(
                            text = "No products found",
                            color = Color.Gray,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            // Selected product section
            selectedProduct?.let { product ->

                HorizontalDivider()

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .verticalScroll(rememberScrollState())
                ) {

                    Text(
                        text = "Selected: ${product.name}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Text("Price per item: ₹${product.price}")
                    Text("Available stock: ${product.quantity}")

                    OutlinedTextField(
                        value = quantityText,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() }) {
                                quantityText = input
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Enter quantity") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        singleLine = true
                    )

                    Text(
                        text = "Item Total: ₹${product.price * quantity}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            if (quantity <= 0) {
                                Toast.makeText(
                                    context,
                                    "Enter a valid quantity",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (quantity > product.quantity) {
                                Toast.makeText(
                                    context,
                                    "Insufficient stock!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val existingIndex =
                                    selectedProducts.indexOfFirst {
                                        it.productId == product.id
                                    }

                                selectedProducts =
                                    if (existingIndex >= 0) {
                                        selectedProducts.mapIndexed { index, item ->
                                            if (index == existingIndex) {
                                                val updatedQuantity =
                                                    item.quantity + quantity

                                                item.copy(
                                                    quantity = updatedQuantity,
                                                    totalPrice =
                                                        item.price * updatedQuantity
                                                )
                                            } else {
                                                item
                                            }
                                        }
                                    } else {
                                        selectedProducts + BillItem(
                                            productId = product.id,
                                            productName = product.name,
                                            price = product.price,
                                            quantity = quantity,
                                            totalPrice = product.price * quantity
                                        )
                                    }

                                selectedProduct = null
                                quantityText = "1"
                                searchText = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = quantity > 0 &&
                                quantity <= product.quantity
                    ) {
                        Text("Add to Bill")
                    }

                    TextButton(
                        onClick = {
                            selectedProduct = null
                            quantityText = "1"
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Cancel Selection")
                    }
                }
            }

            // Bill summary
            HorizontalDivider()

            Text(
                text = "Bill Items: $totalBillItems",
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "Grand Total: ₹$grandTotal",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF008577)
            )

            if (selectedProducts.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 90.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    selectedProducts.forEach { item ->
                        Text(
                            text = "${item.productName} × ${item.quantity} = ₹${item.totalPrice}",
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (selectedProducts.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Please add products to the bill",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        isSaving = true

                        val newSale = Sale(
                            items = selectedProducts,
                            totalAmount = grandTotal,
                            totalItems = totalBillItems,
                            timestamp = System.currentTimeMillis()
                        )

                        salesRepository.saveBillAndUpdateStock(
                            sale = newSale,
                            onSuccess = {
                                isSaving = false

                                Toast.makeText(
                                    context,
                                    "Bill saved successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                navController.popBackStack()
                            },
                            onFailure = { error ->
                                isSaving = false

                                Toast.makeText(
                                    context,
                                    error,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                enabled = selectedProducts.isNotEmpty() && !isSaving
            ) {
                Text(
                    text = if (isSaving) "Saving Bill..." else "Save Bill"
                )
            }
        }
    }
}