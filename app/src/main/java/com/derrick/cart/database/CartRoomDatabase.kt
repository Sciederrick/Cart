package com.derrick.cart.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.derrick.cart.R
import com.derrick.cart.models.Checklist
import com.derrick.cart.models.ChecklistItem

@Database(entities = [Checklist::class, ChecklistItem::class], exportSchema = true, version = 1)
abstract class CartRoomDatabase : RoomDatabase() {
    abstract fun checklistDao(): ChecklistDao
    abstract fun checklistItemDao(): ChecklistItemDao

    companion object {
        // For Singleton instantiation
        @Volatile private var instance: CartRoomDatabase? = null

        fun getInstance(context: Context): CartRoomDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): CartRoomDatabase {
            return Room.databaseBuilder(context, CartRoomDatabase::class.java, R.string.database_name.toString())
                .build()
        }

    }
}