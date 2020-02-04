package com.heady.di.main


import androidx.lifecycle.ViewModel
import com.heady.di.ViewModelKey
import com.heady.ui.viewmodel.CommonViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(CommonViewModel::class)
    abstract fun bindCommonViewModel(viewModel: CommonViewModel): ViewModel

}