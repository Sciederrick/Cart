package com.derrick.cart.repository

import androidx.lifecycle.LiveData
import com.derrick.cart.database.CartRoomDatabase
import com.derrick.cart.models.Checklist

class CartRepository(private val database: CartRoomDatabase) {
    private val checklistDao = database.checklistDao()
    val allChecklists: LiveData<List<Checklist>> = checklistDao.getAll
}