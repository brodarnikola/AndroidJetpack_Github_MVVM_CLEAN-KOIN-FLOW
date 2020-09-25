package com.vjezba.data.database.mapper

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.vjezba.data.database.model.LanguagesDb
import com.vjezba.data.database.model.RepositoryDetailsResponseDb
import com.vjezba.data.database.model.SavedAndAllLanguagesDb
import com.vjezba.data.database.model.SavedLanguagesDb
import com.vjezba.data.networking.model.RepositoryDetailsResponseApi
import com.vjezba.data.networking.model.RepositoryResponseApi
import com.vjezba.domain.model.*
import kotlinx.coroutines.flow.Flow

interface DbMapper {

    fun mapDbLanguagesToDomainLanguages(languages: List<LanguagesDb>): List<Languages>

    fun mapDbLanguageToDomainLanguage(language: LanguagesDb): Languages

    fun mapDbSavedLanguagesToDomainSavedLanguages(language: List<SavedAndAllLanguagesDb>): List<SavedAndAllLanguages>


    fun mapApiResponseGithubToDomainGithub(responseApi: PagingData<RepositoryDetailsResponseApi>): PagingData<RepositoryDetailsResponse>


    fun mapApiResponseGithubToGithubDb(responseApi: List<RepositoryDetailsResponseApi>): List<RepositoryDetailsResponseDb>


    fun mapPagingRepositoryDetailsResponseDbToPagingRepositoryDetailsResponse(responseApi: PagingData<RepositoryDetailsResponseDb>): PagingData<RepositoryDetailsResponse>

}