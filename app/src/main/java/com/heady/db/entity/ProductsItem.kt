package com.heady.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.heady.db.TaxConverter
import com.heady.db.VariantItemConverter

@Entity(tableName = "products")
data class ProductsItem(
    @SerializedName("date_added")
    var dateAdded: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("tax")
    @TypeConverters(TaxConverter::class)
    var tax: Tax,
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0,
    @TypeConverters(VariantItemConverter::class)
    @SerializedName("variants")
    var variants: List<VariantsItem>?
)