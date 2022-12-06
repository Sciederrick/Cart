package com.derrick.cart

import android.app.Application
import com.derrick.cart.data.local.database.CartRoomDatabase
import com.derrick.cart.data.local.entities.Checklist
import com.derrick.cart.data.repository.CartRepository


class CartApplication: Application() {

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val database by lazy { CartRoomDatabase.getInstance(this) }
    val repository by lazy { CartRepository(database.checklistDao(), database.checklistItemDao()) }

}