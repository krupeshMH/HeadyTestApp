package com.heady.db

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import com.heady.db.entity.*
import java.util.*
import java.util.Collections.emptyList


class RatingItemConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<RankingsItem> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<RankingsItem>>() {

        }.type

        return gson.fromJson<List<RankingsItem>>(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<RankingsItem>): String {
        return gson.toJson(someObjects)
    }


}


class ProductItemConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<ProductsItem> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<ProductsItem>>() {

        }.type

        return gson.fromJson<List<ProductsItem>>(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<ProductsItem>): String {
        return gson.toJson(someObjects)
    }


}

class ProductItemRatingConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<ProductsItemRanking> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<ProductsItemRanking>>() {

        }.type

        return gson.fromJson<List<ProductsItemRanking>>(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<ProductsItemRanking>): String {
        return gson.toJson(someObjects)
    }


}


class VariantItemConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<VariantsItem> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<VariantsItem>>() {

        }.type

        return gson.fromJson<List<VariantsItem>>(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<VariantsItem>): String {
        return gson.toJson(someObjects)
    }


}


class CategoryItemConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<CategoriesItem> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<CategoriesItem>>() {

        }.type

        return gson.fromJson<List<CategoriesItem>>(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<CategoriesItem>): String {
        return gson.toJson(someObjects)
    }


}

class TaxConverter {
    @TypeConverter
    fun stringToOutboxItem(string: String): Tax? {
        if (TextUtils.isEmpty(string))
            return null
        return Gson().fromJson(string, Tax::class.java)
    }

    @TypeConverter
     fun outboxItemToString(outboxItem: Tax): String {
        return Gson().toJson(outboxItem)
    }
}








