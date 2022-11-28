package com.derrick.cart.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(
    tableName = "checklist_item", foreignKeys = [ForeignKey(
        entity = Checklist::class,
        childColumns = ["checklist_id"],
        parentColumns = ["id"],
        onDelete = CASCADE
    )]
)

@Serializable
data class ChecklistItem(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "checklist_id", index = true) var checklistId: Long,
    var title: String,
    var description: String? = "",
    var quantity: Float? = 0F,
    var price: Double? = 0.0,
    @ColumnInfo(name="image_URI") var imageURI: String? = "",
    @ColumnInfo(name = "is_done") var isDone: Boolean = false,
    @ColumnInfo(name = "has_sublist") var hasSublist: Boolean = false
)

fun ChecklistItem.formattedQuantity() :String {
    val quantity = quantity.toString()
    val (significand, mantissa) = quantity.split(".")
    val mantissaInt = mantissa.toInt()
    if (mantissaInt > 0) {
        val result = significand.plus(".").plus(mantissa.replace("0", ""))
        return "%.1f".format(result.toFloat()).plus("x")
    }
    return significand.plus("x")
}

fun ChecklistItem.formattedPrice() :String {
    return "%.2f".format(price)
}