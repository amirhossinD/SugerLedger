package com.aht.sugerledger.domain.model

/**
 * Domain model — the shape of a product as the rest of the app understands it.
 *
 * We keep this separate from [com.aht.sugerledger.data.local.entity.ProductEntity] so UI and
 * business logic do not depend on Room/database details. The Repository converts between them.
 */
data class Product(
    val id: Long = 0,
    val name: String,
    val barcode: String = "",
    val category: String = "Other",
    val price: Double,
    val costPrice: Double = 0.0,
    val stockQuantity: Int = 0,
    val lowStockThreshold: Int = 5,
    val unit: String = "piece",
    val notes: String = ""
) {
    /** True when stock is at or below the alert threshold — useful for warehouse warnings. */
    val isLowStock: Boolean
        get() = stockQuantity <= lowStockThreshold
}
