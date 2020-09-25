/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vjezba.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vjezba.data.database.AppDatabase
import com.vjezba.data.database.mapper.DbMapper
import com.vjezba.data.networking.GithubRepositoryApi
import com.vjezba.domain.model.RepositoryDetailsResponse
import com.vjezba.domain.repository.GithubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * RepositoryResponseApi module for handling data operations.
 */
class GithubRepositoryImpl  constructor(
    private val db: AppDatabase,
    private val service: GithubRepositoryApi,
    private val dbMapper: DbMapper)
    : GithubRepository   {

    override fun getSearchRepositoriesResultStream(query: String): Flow<PagingData<RepositoryDetailsResponse>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { GithubRepositorySource(service, query) }
        ).flow.map { dbMapper.mapApiResponseGithubToDomainGithub(it) }
    }

    override fun getSearchRepositoriesWithMediatorAndPaggingData(query: String, pageSize: Int): Flow<PagingData<RepositoryDetailsResponse>> {
        val repositoryDetailsResponse = Pager(
            config = PagingConfig(pageSize),
            remoteMediator = PageKeyedRemoteMediator(db, service, query, dbMapper, 0)
        ) {
            db.languagesRepositoriesDAO().getLanguageRepoWithRemoteMediatorAndPagging(query)
        }.flow  //.map { dbMapper.mapPagingRepositoryDetailsResponseDbToPagingRepositoryDetailsResponse(it) }
        return repositoryDetailsResponse.map { dbMapper.mapPagingRepositoryDetailsResponseDbToPagingRepositoryDetailsResponse(it) }
    }

    /*override fun getSearchRepositoriesWithMediatorAndPaggingData(query: String, pageSize: Int) = Pager(
        config = PagingConfig(pageSize),
        remoteMediator = PageKeyedRemoteMediator(db, service, query, dbMapper)
    ) {
       db.languagesRepositoriesDAO().getLanguageRepoWithRemoteMediatorAndPagging(query)
    }.flow*/

    companion object {
        private const val NETWORK_PAGE_SIZE = 25

        /* // For Singleton instantiation
        @Volatile private var instance: GithubRepository? = null

        fun getInstance(languages: GithubRepositoryApi, dbMapper: DbMapper) =
            instance ?: synchronized(this) {
                instance ?: GithubRepositoryImpl(languages, dbMapper).also { instance = it }
            }*/
    }


}
