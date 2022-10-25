package com.derrick.cart.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.derrick.cart.models.ChecklistItem
import com.derrick.cart.repository.CartRepository

class SubItemsActivityViewModel(private val cartRepository: CartRepository): ViewModel() {
    fun getChecklistItemsByChecklistId(checklistId: Long) :LiveData<List<ChecklistItem>>{
        return cartRepository.getChecklistItemsByChecklistId(checklistId)
    }
}