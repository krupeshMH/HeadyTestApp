package com.heady.network

import com.heady.db.entity.Shopping
import io.reactivex.Flowable
import retrofit2.http.GET

interface MainApi {

    @GET("/json")
    fun getList(): Flowable<Shopping>
}