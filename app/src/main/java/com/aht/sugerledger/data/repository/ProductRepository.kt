package com.aht.sugerledger.data.repository

import com.aht.sugerledger.data.local.dao.ProductDao
import com.aht.sugerledger.data.mapper.toDomain
import com.aht.sugerledger.data.mapper.toEntity
import com.aht.sugerledger.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository — the single source of truth for product data.
 *
 * UI/ViewModel talk to the Repository, not directly to Room.
 * This keeps database details hidden and makes testing easier later.
 */
class ProductRepository(
    private val productDao: ProductDao
) {

    /** Expose domain models; map entities inside the repository. */
    fun observeProducts(): Flow<List<Product>> =
        productDao.observeAllProducts().map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun saveProduct(product: Product): Long {
        val now = System.currentTimeMillis()
        val existingCreatedAt = if (product.id != 0L) {
            productDao.getProductById(product.id)?.createdAt
        } else {
            null
        }
        val entity = product.toEntity(
            nowMillis = now,
            createdAt = existingCreatedAt ?: now
        )
        return productDao.upsert(entity)
    }

    suspend fun deleteProduct(product: Product) {
        val entity = product.toEntity()
        productDao.delete(entity)
    }

    suspend fun adjustStock(productId: Long, delta: Int) {
        productDao.adjustStock(productId, delta)
    }
}
