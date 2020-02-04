package com.heady.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.heady.db.ProductItemConverter
import com.heady.db.ProductItemRatingConverter

@Entity(tableName = "rankings")
data class RankingsItem(
    @PrimaryKey
    @SerializedName("ranking")
    var ranking: String = "",
    @SerializedName("products")
    @TypeConverters(ProductItemRatingConverter::class)
    var products: List<ProductsItemRanking>?
)