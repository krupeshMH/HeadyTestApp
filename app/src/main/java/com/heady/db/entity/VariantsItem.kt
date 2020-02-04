package com.heady.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class VariantsItem(
    @SerializedName("color")
    var color: String = "",
    @SerializedName("size")
    var size: Int = 0,
    @SerializedName("price")
    var price: Int = 0,
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0
)