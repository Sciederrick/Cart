package com.derrick.cart.data.viewmodels

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.derrick.cart.data.repository.CartRepository
import com.derrick.cart.data.local.entities.Checklist
import com.derrick.cart.R
import com.derrick.cart.data.local.daos.ChecklistDao
import com.derrick.cart.data.local.daos.ChecklistItemDao
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class ItemsActivityViewModel(private val checklistDao: ChecklistDao, checklistItemDao: ChecklistItemDao) : ViewModel() {
//    val allChecklists: LiveData<List<Checklist>> = cartRepository.allChecklists

    val pagedChecklists: Flow<PagingData<Checklist>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true,
            maxSize = 200
        )) {
        checklistDao.pagedChecklists
    }
        .flow
        .cachedIn(viewModelScope)

    private fun insertChecklist(checklist: Checklist) = viewModelScope.launch {
        checklistDao.insert(checklist)
    }

    private fun getNewChecklistEntry(title: String, tags: String, itemsChecked: Long, size: Long) : Checklist {
        return Checklist(title = title, tags = tags, itemsChecked = itemsChecked, size = size)
    }

    fun addNewChecklist(title: String, tags: String = "", itemsChecked: Long = 0, size: Long = 0) {
        val newChecklist = getNewChecklistEntry(title, tags, itemsChecked, size)
        insertChecklist(newChecklist)
    }

    fun isEntryValid(checklistTitle: String): Boolean {
        if (checklistTitle.isBlank()) {
            return false
        }
        return true
    }

    fun updateChecklist(checklist: Checklist) = viewModelScope.launch {
        checklistDao.update(checklist)
    }

    fun deleteChecklist(checklist: Checklist) = viewModelScope.launch {
        checklistDao.delete(checklist)
    }

    private fun checklistsAsync(ids: List<Int>) = viewModelScope.async {
        checklistDao.getByIds(ids)
    }

    fun getChecklistsByTitleOrItemsCheckedOrTags(
        searchQuery: String
    ) :LiveData<List<Checklist>> {
        return checklistDao.getByTitleOrItemsCheckedOrTags("%$searchQuery%")
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
    companion object {
        const val PAGE_SIZE = 50
    }
}

class ItemsActivityViewModelFactory(private val checklistDao: ChecklistDao, private val checklistItemDao: ChecklistItemDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemsActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ItemsActivityViewModel(checklistDao, checklistItemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
