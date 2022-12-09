package com.derrick.cart.data.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.derrick.cart.data.local.daos.ChecklistDao
import com.derrick.cart.data.local.daos.ChecklistItemDao
import com.derrick.cart.data.local.entities.Checklist
import com.derrick.cart.data.local.entities.ChecklistItem



class CartRepository(private val checklistDao: ChecklistDao,
                     private val checklistItemDao: ChecklistItemDao
) {
//    val allChecklists: LiveData<List<Checklist>> = checklistDao.getAll

//    val checklistPagingSource = ChecklistPagingSource(checklistDao)
    val pagedChecklists = checklistDao.pagedChecklists

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