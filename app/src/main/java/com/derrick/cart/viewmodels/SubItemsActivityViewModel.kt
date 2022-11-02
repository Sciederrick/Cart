package com.derrick.cart.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.derrick.cart.models.ChecklistItem
import com.derrick.cart.repository.CartRepository

class SubItemsActivityViewModel(private val cartRepository: CartRepository): ViewModel() {
    fun getChecklistItemsByChecklistId(checklistId: Long) :LiveData<List<ChecklistItem>>{
        return cartRepository.getChecklistItemsByChecklistId(checklistId)
    }
}

class SubItemsActivityViewModelFactory(private val cartRepository: CartRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubItemsActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SubItemsActivityViewModel(cartRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}