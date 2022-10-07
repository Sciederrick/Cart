package com.derrick.cart.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checklist")
data class Checklist (
    @PrimaryKey(autoGenerate = true) val id: Int,
    var title: String?,
    var tags: String?,
    @ColumnInfo(name = "items_checked") var itemsChecked: Int?
)