package com.derrick.cart.data.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.derrick.cart.data.local.entities.ChecklistItem
import com.derrick.cart.data.repository.CartRepository
import kotlinx.coroutines.launch

class SubItemsActivityViewModel(private val cartRepository: CartRepository): ViewModel() {
    fun getChecklistItemsByChecklistId(checklistId: Long) :LiveData<List<ChecklistItem>>{
        return cartRepository.getChecklistItemsByChecklistId(checklistId)
    }

    fun deleteChecklistItem(checklistItem: ChecklistItem) = viewModelScope.launch {
        cartRepository.deleteChecklistItem(checklistItem)
    }

    fun insertChecklistItem(checklistItem: ChecklistItem) = viewModelScope.launch {
        cartRepository.insertChecklistItem(checklistItem)
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