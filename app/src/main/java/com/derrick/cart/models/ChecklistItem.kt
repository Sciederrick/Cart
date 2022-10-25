package com.derrick.cart.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "checklist_item", foreignKeys = [ForeignKey(
    entity = Checklist::class,
    childColumns = ["checklist_id"],
    parentColumns = ["id"]
)]
)

@Serializable
data class ChecklistItem (
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "checklist_id", index = true) var checklistId: Long,
    var title: String?,
    var description: String?,
    var quantity: Float?,
    var price: Double?,
    @ColumnInfo(name = "is_done") var isDone: Boolean,
    @ColumnInfo(name = "has_sublist") var hasSublist: Boolean
)