/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.vjezba.androidjetpackgithub.viewmodels

import androidx.lifecycle.*
import com.vjezba.data.Post
import com.vjezba.data.networking.FlowRepositoryApi
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

@ExperimentalCoroutinesApi
@FlowPreview
class FlowMultipleExamplesViewModel : ViewModel() {


    private val _postData: MutableLiveData<List<Post>> = MutableLiveData()
    val postData: LiveData<List<Post>> = _postData

    private val _commentData: MutableLiveData<Post> = MutableLiveData()
    val commentData: LiveData<Post> = _commentData

    init {
        viewModelScope.launch {
            getAllPosts()
        }
    }

    private suspend fun getAllPosts() {
        val retrofit = setupRetrofitFlow()

        flowOf(retrofit.getPosts())
            .flatMapLatest {
                delay(1500)
                _postData.value = it
                it.asFlow()
            }
            .onEach {

                // First example to launch new flow ( to get data -> comments for every posts )
                // In this it is launched 100 coroutines
//                val postData = it
//                flowOf(retrofit.getComments(it.id))
//                    .map {
//
//                        val delayFlow: Int =
//                            (Random().nextInt(3) + 1) * 1000 // sleep thread for x ms
//                        delay(delayFlow.toLong())
//
//                        postData.comments = it
//                        _commentData.value = postData
//                    }
//                    .launchIn(viewModelScope)

                // Second example to launch new flow ( to get data -> comments for every posts )
                // In this it is launched 100 coroutines
//                viewModelScope.launch {
//                    val delayFlow: Int = (Random().nextInt(3) + 1) * 1000 // sleep thread for x ms
//                    delay(delayFlow.toLong())
//
//                    val listComments = retrofit.getComments(it.id)
//
//                    it.comments = listComments
//                    _commentData.value = it
//                }

                // Third example to launch new flow ( to get data -> comments for every posts )
                // In this it is launched 100 coroutines
                flow {

                    val delayFlow: Int = (Random().nextInt(3) + 1) * 1000 // sleep thread for x ms
                    delay(delayFlow.toLong())

                    val listComments = retrofit.getComments(it.id)
                    emit(listComments)

                    it.comments = listComments
                    _commentData.value = it

                }.launchIn(viewModelScope)
            }
            .launchIn(viewModelScope)

    }

    private suspend fun setupRetrofitFlow(): FlowRepositoryApi {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(FlowRepositoryApi::class.java)
    }


}
