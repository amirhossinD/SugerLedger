package com.aht.sugerledger.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Entity — maps one Kotlin class to one SQLite table row.
 *
 * @Entity tells Room this class represents the "products" table.
 * @PrimaryKey autoGenerate = true means Room assigns IDs automatically on insert.
 */
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val barcode: String,
    val category: String,
    val price: Double,
    val costPrice: Double,
    val stockQuantity: Int,
    val lowStockThreshold: Int,
    val unit: String,
    val notes: String,
    val createdAt: Long,
    val updatedAt: Long
)
