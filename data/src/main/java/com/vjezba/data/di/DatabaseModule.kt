package com.vjezba.data.di

import com.vjezba.data.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val WEATHER_DB = "programing-domainLanguages-database"

val databaseModule = module {
  
  single { AppDatabase.getInstance(androidContext()) }

  factory { get<AppDatabase>().languagesDAO() }
  factory { get<AppDatabase>().savedLanguagesDAO() }
}