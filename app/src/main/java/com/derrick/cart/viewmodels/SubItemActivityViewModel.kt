package com.derrick.cart.viewmodels

import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.derrick.cart.models.Checklist
import com.derrick.cart.models.ChecklistItem
import com.derrick.cart.repository.CartRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class SubItemActivityViewModel(private val cartRepository: CartRepository) : ViewModel() {
    fun getAllChecklistsAsync() = viewModelScope.async {
        cartRepository.getChecklistsAsync()
    }

    private fun insertChecklistItem(checklistItem: ChecklistItem) = viewModelScope.launch {
        cartRepository.insertChecklistItem(checklistItem)
    }

    fun getNewChecklistItemEntry(
        checklistId: Long,
        title: String,
        description: String,
        quantity: Float,
        price: Double,
        hasSublist: Boolean,
        isDone: Boolean
    ): ChecklistItem {
        return ChecklistItem(
            checklistId = checklistId,
            title = title,
            description = description,
            quantity = quantity,
            price = price,
            hasSublist = hasSublist,
            isDone = isDone
        )
    }

    fun isEntryValid(payload: HashMap<String, Any>) :Boolean{
        val title = payload["title"] as String
        if (title.isBlank()) {
            return false
        }
        return true
    }

    fun addChecklistItem(payload: HashMap<String, Any>) {
        val newChecklistItem = getNewChecklistItemEntry(
            checklistId = payload["checklistId"] as Long,
            title = payload["title"] as String,
            description = payload["description"] as String,
            quantity = payload["quantity"] as Float,
            price = payload["price"] as Double,
            hasSublist = payload["hasSublist"] as Boolean,
            isDone = payload["isDone"] as Boolean
        )
        insertChecklistItem(newChecklistItem)
    }

}

class SubItemActivityViewModelFactory(private val cartRepository: CartRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubItemActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SubItemActivityViewModel(cartRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}