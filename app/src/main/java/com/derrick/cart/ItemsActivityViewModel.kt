package com.derrick.cart

import android.os.Bundle
import androidx.lifecycle.ViewModel

class ItemsActivityViewModel : ViewModel() {
    var isNewlyCreated = true

    var navDrawerDysplaySelectionName =
        "com.israteneda.notekeeper.ItemsActivity.navDrawerDisplaySelection"
    var recentlyViewedListIdsName =
        "com.israteneda.notekeeper.ItemsActivityViewModel.recentlyViewedListIds"

    var navDrawerDisplaySelection = R.id.nav_lists

    private val maxRecentlyViewedLists = 3
    val recentlyViewedLists = ArrayList<ListInfo>(maxRecentlyViewedLists)

    fun addToRecentlyViewedLists(list: ListInfo) {
        val existingIndex = recentlyViewedLists.indexOf(list)
        if (existingIndex == -1) {
            recentlyViewedLists.add(0, list)
            for (index in recentlyViewedLists.lastIndex downTo maxRecentlyViewedLists)
                recentlyViewedLists.removeAt(index)
        } else {
            for (index in (existingIndex - 1) downTo 0)
                recentlyViewedLists[index + 1] = recentlyViewedLists[index]
            recentlyViewedLists[0] = list
        }
    }

    fun saveState(outState: Bundle) {
        outState.putInt(navDrawerDysplaySelectionName, navDrawerDisplaySelection)
        val notesId = DataManager.listIdsAsIntArray(recentlyViewedLists)
        outState.putIntArray(recentlyViewedListIdsName, notesId)
    }

    fun restoreState(savedInstanceState: Bundle) {
        navDrawerDisplaySelection = savedInstanceState.getInt(navDrawerDysplaySelectionName)
        val notesIds = savedInstanceState.getIntArray(recentlyViewedListIdsName)
        val noteList = notesIds?.let { DataManager.loadLists(*it) }
        if (noteList != null) recentlyViewedLists.addAll(noteList)
    }
}