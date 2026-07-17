package com.aht.sugerledger.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aht.sugerledger.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) — defines SQL operations for the products table.
 *
 * Room generates the implementation at compile time via KSP.
 * Returning Flow<List<...>> means the UI automatically updates when data changes.
 */
@Dao
interface ProductDao {

    /** Observe all products, newest first. Flow re-emits whenever the table changes. */
    @Query("SELECT * FROM products ORDER BY name ASC")
    fun observeAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: Long): ProductEntity?

    /** REPLACE strategy updates the row if the same primary key already exists. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(product: ProductEntity): Long

    @Update
    suspend fun update(product: ProductEntity)

    @Delete
    suspend fun delete(product: ProductEntity)

    /** Quick stock adjustment without loading the full product into the UI layer. */
    @Query("UPDATE products SET stockQuantity = stockQuantity + :delta, updatedAt = :updatedAt WHERE id = :productId")
    suspend fun adjustStock(productId: Long, delta: Int, updatedAt: Long = System.currentTimeMillis())
}
