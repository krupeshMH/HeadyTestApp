package com.heady.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tax")
data class Tax(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("tax_id")
    var tax_id: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("value")
    var value: Double = 0.0
)