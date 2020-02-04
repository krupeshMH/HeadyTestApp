package com.heady.di.main


import com.heady.ui.category.CategoriesFragment
import com.heady.ui.home.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    internal abstract fun contributeHomeListFragment(): HomeFragment

    @ContributesAndroidInjector
    internal abstract fun contributeCategoryListFragment(): CategoriesFragment


}
