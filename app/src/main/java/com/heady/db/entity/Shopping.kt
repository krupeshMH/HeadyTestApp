package com.heady.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.heady.db.CategoryItemConverter
import com.heady.db.RatingItemConverter

@Entity(tableName = "shopping")
data class Shopping(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("rankings")
    @TypeConverters(RatingItemConverter::class)
    var rankings: List<RankingsItem>?,
    @SerializedName("categories")
    @TypeConverters(CategoryItemConverter::class)
    var categories: List<CategoriesItem>?
)