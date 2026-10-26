package com.example.stockmate
import com.google.firebase.firestore.FirebaseFirestore

class ProductRepository {

    private val db = FirebaseFirestore.getInstance()

    fun addProduct(
        product: Product,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val document = db.collection("products").document()

        val productWithId = product.copy(id = document.id)

        document.set(productWithId)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(
                    exception.message ?: "Failed to add product"
                )
            }
    }
    fun updateProduct(
        product: Product,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (product.id.isBlank()) {
            onFailure("Product ID is missing")
            return
        }

        db.collection("products")
            .document(product.id)
            .set(product)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Failed to update product")
            }
    }

    fun deleteProduct(
        productId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (productId.isBlank()) {
            onFailure("Product ID is missing")
            return
        }

        db.collection("products")
            .document(productId)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Failed to delete product")
            }
    }
    fun listenToProducts(
        onProductsChanged: (List<Product>) -> Unit,
        onFailure: (String) -> Unit
    ): com.google.firebase.firestore.ListenerRegistration {

        return db.collection("products")
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    onFailure(
                        error.localizedMessage ?: "Unable to load products"
                    )
                    return@addSnapshotListener
                }

                val products = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(Product::class.java)
                        ?.copy(id = document.id)
                } ?: emptyList()

                onProductsChanged(products)
            }
    }
}