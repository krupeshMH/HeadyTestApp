package com.heady.db.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.heady.db.entity.CategoriesItem
import com.heady.db.entity.ProductsItem

data class CategoryProduct(
    @Embedded val categoriesItem: CategoriesItem,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"

    ) val product: ProductsItem
)