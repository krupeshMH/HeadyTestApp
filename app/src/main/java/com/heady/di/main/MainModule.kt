package com.heady.di.main

import com.heady.network.MainApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit


@Module
class MainModule {

    @MainScope
    @Provides
    internal fun provideMainApi(retrofit: Retrofit): MainApi {
        return retrofit.create<MainApi>(MainApi::class.java)
    }
}