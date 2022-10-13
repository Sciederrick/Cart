package com.derrick.cart.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.derrick.cart.database.ChecklistDao
import com.derrick.cart.models.Checklist


class CartRepository(private val checklistDao: ChecklistDao) {
    val allChecklists: LiveData<List<Checklist>> = checklistDao.getAll

    suspend fun checklists(ids: List<Int>) = checklistDao.getByIds(ids)

    fun filterChecklists(searchQuery: String): LiveData<List<Checklist>> {
        return checklistDao.getByTitleOrItemsCheckedOrTags(searchQuery)
    }

    @WorkerThread
    suspend fun insert(checklist: Checklist) {
        checklistDao.insert(checklist)
    }

    @WorkerThread
    suspend fun update(checklist: Checklist) {
        checklistDao.update(checklist)
    }

    @WorkerThread
    suspend fun delete(checklist: Checklist) {
        checklistDao.delete(checklist)
    }
}