package com.derrick.cart

import android.app.Application
import com.derrick.cart.data.local.database.CartRoomDatabase
import com.derrick.cart.data.repository.CartRepository
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.SupervisorJob

class CartApplication: Application() {
    // No need to cancel this scope as it'll be torn down with the process
//    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val database by lazy { CartRoomDatabase.getInstance(this) }
    val repository by lazy { CartRepository(database.checklistDao(), database.checklistItemDao()) }
}