package com.heady.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.heady.db.entity.*

@Database(
    entities = [
        Shopping::class, ProductsItem::class, Tax::class,
        RankingsItem::class, CategoriesItem::class, VariantsItem::class, ProductsItemRanking::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    RatingItemConverter::class,
    CategoryItemConverter::class,
    ProductItemConverter::class,
    ProductItemRatingConverter::class,
    VariantItemConverter::class, TaxConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoriesDAO(): CategoriesDAO

}