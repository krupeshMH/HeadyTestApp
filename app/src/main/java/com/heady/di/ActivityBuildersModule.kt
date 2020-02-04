package com.heady.di

import com.heady.MainActivity
import com.heady.di.main.MainFragmentBuildersModule
import com.heady.di.main.MainModule
import com.heady.di.main.MainScope
import com.heady.di.main.MainViewModelsModule


import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuildersModule {

    @MainScope
    @ContributesAndroidInjector(modules = [MainModule::class, MainViewModelsModule::class, MainFragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity
}