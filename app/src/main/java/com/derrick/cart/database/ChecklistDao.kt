package com.derrick.cart.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.derrick.cart.models.Checklist

@Dao
interface ChecklistDao {
    @Insert
    suspend fun insert(checklist: Checklist)

    @get:Query("SELECT * FROM checklist")
    val getAll: LiveData<List<Checklist>>

    @Query("SELECT * FROM checklist WHERE id IN (:ids)")
    suspend fun getByIds(ids: List<Int>): List<Checklist>

    @Query("SELECT * FROM checklist WHERE title LIKE :searchString OR items_checked LIKE :searchString" +
            " OR tags LIKE :searchString")
    suspend fun getByTitleOrItemsCheckedOrTags(searchString: String): List<Checklist>

    @Update
    suspend fun update(checklist: Checklist)

    @Delete
    suspend fun delete(checklist: Checklist)

}