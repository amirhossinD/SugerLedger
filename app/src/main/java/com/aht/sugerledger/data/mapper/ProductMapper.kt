package com.aht.sugerledger.data.mapper

import com.aht.sugerledger.data.local.entity.ProductEntity
import com.aht.sugerledger.domain.model.Product

/**
 * Mapper functions convert between database entities and domain models.
 *
 * Keeping conversion in one place avoids duplicating mapping logic across the app.
 */
fun ProductEntity.toDomain(): Product = Product(
    id = id,
    name = name,
    barcode = barcode,
    category = category,
    price = price,
    costPrice = costPrice,
    stockQuantity = stockQuantity,
    lowStockThreshold = lowStockThreshold,
    unit = unit,
    notes = notes
)

fun Product.toEntity(
    nowMillis: Long = System.currentTimeMillis(),
    createdAt: Long = nowMillis
): ProductEntity = ProductEntity(
    id = id,
    name = name.trim(),
    barcode = barcode.trim(),
    category = category,
    price = price,
    costPrice = costPrice,
    stockQuantity = stockQuantity,
    lowStockThreshold = lowStockThreshold,
    unit = unit,
    notes = notes.trim(),
    createdAt = createdAt,
    updatedAt = nowMillis
)
