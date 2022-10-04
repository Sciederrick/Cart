package com.derrick.cart.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Checklist (
    @PrimaryKey(autoGenerate = true) val id: Int,
    var title: String?,
    val tags: String?,
    var itemsChecked: Int
)