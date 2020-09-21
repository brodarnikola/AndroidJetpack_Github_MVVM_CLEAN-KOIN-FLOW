/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vjezba.data.repository

import androidx.paging.PagingData
import com.vjezba.data.database.AppDatabase
import com.vjezba.domain.model.RepositoryDetailsResponse
import com.vjezba.domain.repository.PaggingWithNetworkAndDbRepository
import kotlinx.coroutines.flow.Flow

/**
 * Repository implementation that uses a database backed [androidx.paging.PagingSource] and
 * [androidx.paging.RemoteMediator] to load pages from network when there are no more items cached
 * in the database to load.
 */
class PaggingWithNetwokrAndDbRepositoryImpl(val db: AppDatabase) : PaggingWithNetworkAndDbRepository {
    override fun exampleOfPaggingWithNetworkAndDb(
        languageName: String,
        pageSize: Int
    ): Flow<PagingData<RepositoryDetailsResponse>> {
        TODO("Not yet implemented")
    }

    /*override fun exampleOfPaggingWithNetworkAndDb(
        languageName: String,
        pageSize: Int
     ) = Pager(
        config = PagingConfig(pageSize),
        remoteMediator = PageKeyedRemoteMediator(db, redditApi, subReddit)
    ) {
        db.posts().postsBySubreddit(subReddit)
    }.flow*/


   /* (subReddit: String, pageSize: Int) = Pager(
        config = PagingConfig(pageSize),
        remoteMediator = PageKeyedRemoteMediator(db, redditApi, subReddit)
    ) {
        db.posts().postsBySubreddit(subReddit)
    }.flow*/
}
