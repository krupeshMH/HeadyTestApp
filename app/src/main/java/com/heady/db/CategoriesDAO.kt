package com.heady.db

import androidx.room.*
import com.heady.db.entity.CategoriesItem
import com.heady.db.entity.ProductsItem
import com.heady.db.entity.RankingsItem
import com.heady.db.entity.relation.CategoryProduct
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable

@Dao
interface CategoriesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(categoryLis: List<CategoriesItem>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRanking(rankingsItemList: List<RankingsItem>): Completable

    @Query("select * from categories")
    fun getCategories(): Flowable<List<CategoriesItem>>

    @Query("select * from rankings")
    fun getRankingList(): Flowable<List<RankingsItem>>

    @Query("select * from products where id=:productId")
    fun getProduct(productId: Int): Flowable<ProductsItem>

}