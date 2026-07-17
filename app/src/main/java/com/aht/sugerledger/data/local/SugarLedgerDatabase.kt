package com.aht.sugerledger.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aht.sugerledger.data.local.dao.ProductDao
import com.aht.sugerledger.data.local.entity.ProductEntity

/**
 * Room Database — the single entry point to the on-device SQLite database.
 *
 * @Database lists every entity table and the schema version.
 * When you change entities later, bump version and add a Migration (not covered yet).
 */
@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SugarLedgerDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: SugarLedgerDatabase? = null

        /**
         * Singleton pattern — one database instance for the whole app.
         * synchronized + double-check prevents creating multiple connections.
         */
        fun getInstance(context: Context): SugarLedgerDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    SugarLedgerDatabase::class.java,
                    "sugar_ledger.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
