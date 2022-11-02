package com.derrick.cart.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.derrick.cart.models.ChecklistItem
import com.derrick.cart.repository.CartRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class SubItemActivityViewModel(private val cartRepository: CartRepository): ViewModel() {
    fun getAllChecklistsAsync() = viewModelScope.async {
        cartRepository.getChecklistsAsync()
    }

    fun insertChecklistItem(checklistItem: ChecklistItem) = viewModelScope.launch {
        cartRepository.insertChecklistItem(checklistItem)
    }
}

class SubItemActivityViewModelFactory(private val cartRepository: CartRepository) :
    ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubItemActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SubItemActivityViewModel(cartRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}