package com.example.stockmate

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class SalesRepository {

    private val db = FirebaseFirestore.getInstance()
    private val salesCollection = db.collection("sales")

    fun addSale(
        sale: Sale,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val document = salesCollection.document()
        val newSale = sale.copy(id = document.id)

        document.set(newSale)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { error ->
                onFailure(
                    error.localizedMessage ?: "Unable to save bill"
                )
            }
    }


    fun listenToSales(
        onSalesChanged: (List<Sale>) -> Unit,
        onFailure: (String) -> Unit
    ): ListenerRegistration {

        return salesCollection
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    onFailure(
                        error.localizedMessage ?: "Unable to load bills"
                    )
                    return@addSnapshotListener
                }

                val sales = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(Sale::class.java)
                        ?.copy(id = document.id)
                } ?: emptyList()

                onSalesChanged(sales)
            }
    }

    fun saveBillAndUpdateStock(
        sale: Sale,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val salesCollection = db.collection("sales")
        val productsCollection = db.collection("products")

        val billDocument = salesCollection.document()

        db.runTransaction { transaction ->
            val productSnapshots = sale.items.map { item ->
                val productRef = productsCollection.document(item.productId)
                val snapshot = transaction.get(productRef)

                if (!snapshot.exists()) {
                    throw IllegalStateException(
                        "${item.productName} not found"
                    )
                }

                val currentStock =
                    snapshot.getLong("quantity")?.toInt() ?: 0

                if (item.quantity <= 0 || currentStock < item.quantity) {
                    throw IllegalStateException(
                        "Insufficient stock for ${item.productName}"
                    )
                }

                Triple(productRef, snapshot, item)
            }
            productSnapshots.forEach { (productRef, snapshot, item) ->
                val currentStock =
                    snapshot.getLong("quantity")?.toInt() ?: 0

                transaction.update(
                    productRef,
                    "quantity",

                    currentStock - item.quantity
                )
            }

            val newSale = sale.copy(id = billDocument.id)
            transaction.set(billDocument, newSale)

            null
        }.addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener { error ->
            onFailure(
                error.localizedMessage ?: "Failed to save bill"
            )
        }
    }
}