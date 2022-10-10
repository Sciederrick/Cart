package com.derrick.cart.viewmodels

import androidx.lifecycle.*
import com.derrick.cart.repository.CartRepository
import com.derrick.cart.models.Checklist
import com.derrick.cart.R
import kotlinx.coroutines.launch

class ItemsActivityViewModel(private val cartRepository: CartRepository) : ViewModel() {
    val allChecklists: LiveData<List<Checklist>> = cartRepository.allChecklists

    fun insert(checklist: Checklist) = viewModelScope.launch {
        cartRepository.insert(checklist)
    }
    fun update(checklist: Checklist) = viewModelScope.launch {
        cartRepository.update(checklist)
    }

    fun delete(checklist: Checklist) = viewModelScope.launch {
        cartRepository.delete(checklist)
    }

    // ---------------------------------------------------------------------------------------------
    private val maxRecentlyViewedLists = 3

    val recentlyViewedLists = ArrayList<Checklist>(maxRecentlyViewedLists)
    var isNewlyCreated = true
    var navDrawerDisplaySelectionName =
        "com.israteneda.notekeeper.ItemsActivity.navDrawerDisplaySelection"
    var recentlyViewedListIdsName =
        "com.israteneda.notekeeper.ItemsActivityViewModel.recentlyViewedListIds"
    var navDrawerDisplaySelection = R.id.nav_lists

    fun addToRecentlyViewedLists(checklist: Checklist) {
        val existingIndex = recentlyViewedLists.indexOf(checklist)
        if (existingIndex == -1) {
            recentlyViewedLists.add(0, checklist)
            for (index in recentlyViewedLists.lastIndex downTo maxRecentlyViewedLists)
                recentlyViewedLists.removeAt(index)
        } else {
            for (index in (existingIndex - 1) downTo 0)
                recentlyViewedLists[index + 1] = recentlyViewedLists[index]
            recentlyViewedLists[0] = checklist
        }
    }

    //----------------------------------------------------------------------------------------------

//    fun saveState(outState: Bundle) {
//        outState.putInt(navDrawerDisplaySelectionName, navDrawerDisplaySelection)
//        val notesId = DataManager.listIdsAsIntArray(recentlyViewedLists)
//        outState.putIntArray(recentlyViewedListIdsName, notesId)
//    }

//    fun restoreState(savedInstanceState: Bundle) {
//        navDrawerDisplaySelection = savedInstanceState.getInt(navDrawerDisplaySelectionName)
//        val notesIds = savedInstanceState.getIntArray(recentlyViewedListIdsName)
//        val noteList = notesIds?.let { DataManager.loadLists(*it) }
//        if (noteList != null) recentlyViewedLists.addAll(noteList)
//    }
}