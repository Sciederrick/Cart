package com.derrick.cart.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.derrick.cart.repository.CartRepository

class ItemsActivityViewModelFactory(private val cartRepository: CartRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemsActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ItemsActivityViewModel(cartRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}