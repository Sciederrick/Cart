package com.derrick.cart.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.derrick.cart.models.Checklist

@Dao
interface ChecklistDao {
    @Insert
    fun insertAll(vararg checklists: Checklist)

//    @Query("SELECT * FROM checklist")
//    fun getAll(): List<Checklist>

    @get:Query("SELECT * FROM checklist")
    val getAll: LiveData<List<Checklist>>

    @Query("SELECT * FROM checklist WHERE title LIKE :searchString OR itemsChecked LIKE :searchString" +
            " OR tags LIKE :searchString")
    fun getByTitleOrItemsCheckedOrTags(searchString: String): List<Checklist>

    @Update
    fun update(checklist: Checklist)

    @Delete
    fun delete(checklist: Checklist)

}