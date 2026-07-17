package com.aht.sugerledger

import android.app.Application
import com.aht.sugerledger.data.local.SugarLedgerDatabase
import com.aht.sugerledger.data.repository.ProductRepository

/**
 * Custom Application class — created once when the app process starts.
 *
 * We use it here to lazily create shared objects (database, repositories)
 * so every screen can access the same instances without manual wiring everywhere.
 */
class SugarLedgerApplication : Application() {

    /** lazy = created on first access, then reused (singleton-like). */
    val database by lazy { SugarLedgerDatabase.getInstance(this) }

    val productRepository by lazy { ProductRepository(database.productDao()) }
}
