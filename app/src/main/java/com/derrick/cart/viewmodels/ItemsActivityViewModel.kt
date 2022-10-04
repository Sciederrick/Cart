package com.derrick.cart.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.derrick.cart.repository.CartRepository
import com.derrick.cart.database.CartRoomDatabase
import com.derrick.cart.models.Checklist
import com.derrick.cart.R

class ItemsActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val cartRepository = CartRepository(CartRoomDatabase.getInstance(application))
    private val _checklists = MutableLiveData<List<Checklist>>()
    private val maxRecentlyViewedLists = 3

    val allChecklists :LiveData<List<Checklist>> = cartRepository.allChecklists
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