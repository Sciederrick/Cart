package com.derrick.cart.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.derrick.cart.repository.CartRepository


class SearchViewModelFactory(private val cartRepository: CartRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(cartRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}