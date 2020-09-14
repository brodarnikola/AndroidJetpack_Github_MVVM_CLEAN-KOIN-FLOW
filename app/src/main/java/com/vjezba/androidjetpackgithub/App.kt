package com.vjezba.androidjetpackgithub

import android.app.Application
import com.vjezba.androidjetpackgithub.di.presentationModule
import com.vjezba.data.di.databaseModule
import com.vjezba.data.di.networkingModule
import com.vjezba.data.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
  
  companion object {
    lateinit var instance: Application
      private set
  }
  
  override fun onCreate() {
    super.onCreate()
    instance = this

    startKoin {
      androidContext(this@App)
      if (BuildConfig.DEBUG) androidLogger(Level.DEBUG)
      //modules(  dataModules)
      modules(appModules + dataModules)
    }
  }
}

val appModules = listOf(presentationModule)
val dataModules = listOf( networkingModule, repositoryModule, databaseModule)

//val domainModules = listOf(interactionModule)
//val dataModules = listOf(networkingModule, repositoryModule, databaseModule)
