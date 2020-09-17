package com.vjezba.data.database.mapper

import androidx.paging.PagingData
import com.vjezba.data.database.model.LanguagesDb
import com.vjezba.data.database.model.SavedAndAllLanguagesDb
import com.vjezba.data.database.model.SavedLanguagesDb
import com.vjezba.data.networking.model.RepositoryDetailsResponseApi
import com.vjezba.data.networking.model.RepositoryResponseApi
import com.vjezba.domain.model.*

interface DbMapper {


    fun mapDomainSavedAndAllLanguagesToDbSavedAndAllLanguages(savedLanguages: SavedLanguages): SavedLanguagesDb


    fun mapDbLanguagesToDomainLanguages(languages: List<LanguagesDb>): List<com.vjezba.domain.model.Languages>

    fun mapDbLanguageToDomainLanguage(language: LanguagesDb): com.vjezba.domain.model.Languages

    fun mapDbSavedLanguagesToDomainSavedLanguages(language: List<SavedAndAllLanguagesDb>): List<com.vjezba.domain.model.SavedAndAllLanguages>


    fun mapApiResponseGithubToDomainGithub(responseApi: PagingData<RepositoryDetailsResponseApi>): PagingData<RepositoryDetailsResponse>
}