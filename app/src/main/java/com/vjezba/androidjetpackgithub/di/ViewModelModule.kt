package com.vjezba.androidjetpackgithub.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vjezba.androidjetpackgithub.viewmodels.LegoThemeViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LegoThemeViewModel::class)
    abstract fun bindThemeViewModel(viewModel: LegoThemeViewModel): ViewModel



    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
