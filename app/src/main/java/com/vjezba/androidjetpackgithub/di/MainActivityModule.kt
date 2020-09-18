package com.vjezba.androidjetpackgithub.di


import com.vjezba.androidjetpackgithub.ui.activities.LanguagesActivity
import com.vjezba.androidjetpackgithub.ui.activities.LegoThemeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): LanguagesActivity
}
