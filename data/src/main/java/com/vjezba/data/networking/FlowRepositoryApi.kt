/*
 * Copyright 2020 Google LLC
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

package com.vjezba.data.networking

import com.vjezba.data.Comment
import com.vjezba.data.Post
import com.vjezba.data.networking.model.RepositoryResponseApi
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Used to connect to the Unsplash API to fetch photos
 */
interface FlowRepositoryApi {

    @GET("posts")
    suspend fun getPosts(): List<Post> //Response<Flow<List<Post>>>

    @GET("posts/{id}/comments")
    suspend fun getComments(
        @Path("id") id: Int
    ): List<Comment>

    @GET("posts/{id}")
    fun getPost(
        @Path("id") id: Int
    ): Flow<Post>

}
