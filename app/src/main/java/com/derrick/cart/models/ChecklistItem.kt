package com.derrick.cart.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "checklist_item", foreignKeys = [ForeignKey(
    entity = Checklist::class,
    childColumns = ["checklist_id"],
    parentColumns = ["id"]
)]
)
data class ChecklistItem (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "checklist_id") var checklistId: Int,
    var title: String?,
    var description: String?,
    var quantity: Float?,
    var price: Double?,
    @ColumnInfo(name = "is_done") var isDone: Boolean,
    @ColumnInfo(name = "has_sublist") var hasSublist: Boolean
)