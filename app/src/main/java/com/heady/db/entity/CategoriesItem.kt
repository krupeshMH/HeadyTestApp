package com.heady.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.heady.db.ProductItemConverter

@Entity(tableName = "categories")
data class CategoriesItem(

    @SerializedName("name")
    var name: String = "",
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("products")
    @TypeConverters(ProductItemConverter::class)
    var products: List<ProductsItem>?
)