package com.derrick.cart.data.local.daos

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagingSource
import androidx.room.*
import com.derrick.cart.data.local.entities.Checklist

@Dao
interface ChecklistDao {
    @Insert
    suspend fun insert(checklist: Checklist)

//    @get:Query("SELECT * FROM checklist")
//    val getAll: LiveData<List<Checklist>>

//    @get:Query("SELECT * FROM checklist")
//    val getAll: PagingSource<Int, Checklist>

    @get:Query("SELECT * FROM checklist")
    val getAll: DataSource.Factory<Int, Checklist>


    @Query("SELECT * FROM checklist WHERE id IN (:ids)")
    suspend fun getByIds(ids: List<Int>): List<Checklist>

    @Query("SELECT * FROM checklist")
    suspend fun getAllAsync(): List<Checklist>?

    @Query("SELECT * FROM checklist WHERE title LIKE :searchString OR items_checked LIKE :searchString" +
            " OR tags LIKE :searchString")
    fun getByTitleOrItemsCheckedOrTags(searchString: String): LiveData<List<Checklist>>

    @Update
    suspend fun update(checklist: Checklist)

    @Delete
    suspend fun delete(checklist: Checklist)

}