package com.derrick.cart.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Entity(tableName = "checklist")
@Serializable
data class Checklist (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var title: String,
    var tags: String? = "",
    @ColumnInfo(name = "items_checked") var itemsChecked: Long = 0,
    var size: Long = 0
) {
    override fun toString(): String {
        return title
    }
}