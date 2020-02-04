package com.heady.di

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.heady.BuildConfig
import com.heady.db.AppDatabase
import com.heady.db.CategoriesDAO
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import okhttp3.Cache
import okhttp3.OkHttpClient
import io.reactivex.schedulers.Schedulers
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter


@Module
class AppModule {

    @Singleton
    @Provides
    internal fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun provideHttpCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize.toLong())
    }


    @Singleton
    @Provides
    internal fun provideRetrofitInstance(cache: Cache, gson: Gson): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor { chain ->
                val original = chain.request()

                // Customize the request
                val request = original.newBuilder()
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .removeHeader("Pragma")
                    .header("Cache-Control", String.format("max-age=%s", BuildConfig.CACHETIME))
                    .build()

                val response = chain.proceed(request)
                response.cacheResponse()
                // Customize or return the response
                Log.d("API", "HttpLoggingInterceptor: $response")
                response
            }
            .cache(cache)
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(provideRxJavaCallAdapterFactory())
            .build()

    }

    @Provides
    @Singleton
    fun provideRxJavaCallAdapterFactory(): CallAdapter.Factory {
        return RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
    }

    @Singleton
    @Provides
    fun provideRoomInstance(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java, "AppDb.db"
        )
            .build()
    }

    @Singleton
    @Provides
    fun provideCategoriesDAO(database: AppDatabase): CategoriesDAO {
        return database.categoriesDAO()
    }


}