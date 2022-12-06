package com.derrick.cart.data.repository

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.derrick.cart.data.local.daos.ChecklistDao
import com.derrick.cart.data.local.daos.ChecklistItemDao
import com.derrick.cart.data.local.entities.Checklist
import com.derrick.cart.data.local.entities.ChecklistItem
import kotlinx.coroutines.delay


//import com.derrick.cart.data.pagingdatasource.ChecklistPagingSource

class CartRepository(private val checklistDao: ChecklistDao,
                     private val checklistItemDao: ChecklistItemDao
) {
//    fun checklistPagingSource() = ChecklistPagingSource(checklistDao)
    fun getChecklists(): LiveData<PagedList<Checklist>> {
        val factory = checklistDao.getAll
        return LivePagedListBuilder<Int, Checklist>(
            factory,
            PagedList.Config.Builder()
                .setPageSize(50)
                .setPrefetchDistance(2)
                .setEnablePlaceholders(false)
                .build()
        ).build()
    }

//    val allChecklists: LiveData<List<Checklist>> = checklistDao.getAll

    suspend fun checklists(ids: List<Int>) = checklistDao.getByIds(ids)

    fun filterChecklists(searchQuery: String): LiveData<List<Checklist>> {
        return checklistDao.getByTitleOrItemsCheckedOrTags(searchQuery)
    }

    fun getChecklistItemsByChecklistId(checklistId: Long): LiveData<List<ChecklistItem>> {
        return checklistItemDao.getByChecklistId(checklistId)
    }

    suspend fun getChecklistsAsync() :List<Checklist>?{
        return checklistDao.getAllAsync()
    }

    @WorkerThread
    suspend fun insertChecklist(checklist: Checklist) {
        checklistDao.insert(checklist)
    }

    @WorkerThread
    suspend fun insertChecklistItem(checklistItem: ChecklistItem) {
        checklistItemDao.insert(checklistItem)
    }

    @WorkerThread
    suspend fun updateChecklist(checklist: Checklist) {
        checklistDao.update(checklist)
    }

    @WorkerThread
    suspend fun updateChecklistItem(checklistItem: ChecklistItem) {
        checklistItemDao.update(checklistItem)
    }

    @WorkerThread
    suspend fun deleteChecklist(checklist: Checklist) {
        checklistDao.delete(checklist)
    }

    @WorkerThread
    suspend fun deleteChecklistItem(checklistItem: ChecklistItem) {
        checklistItemDao.delete(checklistItem)
    }
}