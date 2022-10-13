package com.derrick.cart.viewmodels

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.derrick.cart.repository.CartRepository
import com.derrick.cart.models.Checklist
import com.derrick.cart.R
import kotlinx.coroutines.*

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

    private fun checklistsAsync(ids: List<Int>) = viewModelScope.async {
        cartRepository.checklists(ids)
    }

    fun getChecklistsByTitleOrItemsCheckedOrTags(
        searchQuery: String
    ) :LiveData<List<Checklist>> {
        return cartRepository.filterChecklists(searchQuery)
    }

    // list view history----------------------------------------------------------------------------
    private val maxRecentlyViewedLists = 3
    var recentlyViewedChecklists = ArrayList<Checklist>(maxRecentlyViewedLists)

    fun addToRecentlyViewedChecklists(checklist: Checklist) {
        val existingIndex = recentlyViewedChecklists.indexOf(checklist)
        if (existingIndex == -1) {
            recentlyViewedChecklists.add(0, checklist)
            for (index in recentlyViewedChecklists.lastIndex downTo maxRecentlyViewedLists)
                recentlyViewedChecklists.removeAt(index)
        } else {
            for (index in (existingIndex - 1) downTo 0)
                recentlyViewedChecklists[index + 1] = recentlyViewedChecklists[index]
            recentlyViewedChecklists[0] = checklist
        }
    }

    fun saveChecklistHistory(context: Context, sharedPref: SharedPreferences) {
        val historyArray = recentlyViewedChecklists.map { it.id.toString() }.toTypedArray()
        with(sharedPref.edit()) {
            putStringSet(
                context.getString(R.string.viewed_checklist_history),
                mutableSetOf(*historyArray)
            )
            apply()
        }
    }

    suspend fun getChecklistHistory(context: Context, sharedPref: SharedPreferences) {
        val viewedChecklistHistoryIds = sharedPref
            .getStringSet(context.getString(R.string.viewed_checklist_history), mutableSetOf())
        val checklistIds = viewedChecklistHistoryIds
            ?.map { it.toInt() }

        if (checklistIds?.isEmpty() == true) return

        recentlyViewedChecklists = checklistsAsync(checklistIds!!).await() as ArrayList<Checklist>

    }

    //----------------------------------------------------------------------------------------------

    var isNewlyCreated = true
    var navDrawerDisplaySelectionName =
        "com.israteneda.notekeeper.ItemsActivity.navDrawerDisplaySelection"
    var recentlyViewedListIdsName =
        "com.israteneda.notekeeper.ItemsActivityViewModel.recentlyViewedListIds"
    var navDrawerDisplaySelection = R.id.nav_lists

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
