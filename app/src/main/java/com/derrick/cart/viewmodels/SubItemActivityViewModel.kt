package com.derrick.cart.viewmodels

import androidx.lifecycle.ViewModel
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