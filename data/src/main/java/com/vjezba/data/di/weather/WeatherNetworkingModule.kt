package com.vjezba.data.di.weather

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.vjezba.data.weather.client.WeatherApiClient
import com.vjezba.data.weather.mapper.ApiMapper
import com.vjezba.data.weather.mapper.ApiMapperImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL_NAME = "BASE_URL"
private const val BASE_URL = "https://www.metaweather.com/api/"


val weatherNetworkingModule = module {

    single(named(BASE_URL_NAME)) {
        BASE_URL
    }

    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single<Retrofit>(named("retrofitWeather")) {
        val retrofitWeather =Retrofit.Builder()
            .baseUrl(get<String>(named(BASE_URL_NAME)))
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .client(get())
            .build()
        retrofitWeather
    }

    single {
        get<Retrofit>(named("retrofitWeather")).create(WeatherApiClient::class.java)
    }

    single<ApiMapper> {
        ApiMapperImpl()
    }

}
