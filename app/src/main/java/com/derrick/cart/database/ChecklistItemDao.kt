package com.derrick.cart.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.derrick.cart.models.ChecklistItem

@Dao
interface ChecklistItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(checklistItem: ChecklistItem)

    @Query("SELECT * FROM checklist_item WHERE checklist_id LIKE :checklistId")
    fun getByChecklistId(checklistId: Long): LiveData<List<ChecklistItem>>

    @Query("SELECT * FROM checklist_item WHERE title LIKE :searchString OR quantity LIKE :searchString" +
            " OR price LIKE :searchString")
    fun getByTitleOrQuantityOrPrice(searchString: String): LiveData<List<ChecklistItem>>

    @Update
    suspend fun update(checklistItem: ChecklistItem)

    @Delete
    suspend fun delete(checklistItem: ChecklistItem)
}