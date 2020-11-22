package com.vjezba.data.di.weather


import com.vjezba.data.weather.WeatherRepositoryImpl
import com.vjezba.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module


private const val MAIN_DISPATCHER = "main_dispatcher"
private const val BACKGROUND_DISPATCHER = "background_dispatcher"


val weatherRepositoryModule = module {

  single<WeatherRepository> {
    WeatherRepositoryImpl(
      get(),
      get(),
      get(named(BACKGROUND_DISPATCHER)),
      get(),
      get()
    )
  }

  single(named(MAIN_DISPATCHER)) { Dispatchers.Main }

  single(named(BACKGROUND_DISPATCHER)) { Dispatchers.IO }

}