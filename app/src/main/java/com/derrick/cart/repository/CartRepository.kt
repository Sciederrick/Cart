package com.derrick.cart.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.derrick.cart.database.ChecklistDao
import com.derrick.cart.models.Checklist


class CartRepository(private val checklistDao: ChecklistDao) {
    val allChecklists: LiveData<List<Checklist>> = checklistDao.getAll

    @WorkerThread
    suspend fun insert(checklist: Checklist) {
        checklistDao.insert(checklist)
    }
}