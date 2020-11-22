package com.vjezba.data.di.weather

import com.vjezba.data.database.AppDatabase
import org.koin.dsl.module


val weatherDatabaseModule = module {

  factory { get<AppDatabase>().forecastDao() }
}