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

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.vjezba.androidjetpackgithub.ui.mapper.ForecastViewState
import com.vjezba.androidjetpackgithub.ui.mapper.LocationViewState
import com.vjezba.androidjetpackgithub.ui.mapper.WeatherViewStateMapper
import com.vjezba.domain.repository.WeatherRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

private const val SEARCH_DELAY_MILLIS = 500L
private const val MIN_QUERY_LENGTH = 2

@ExperimentalCoroutinesApi
@FlowPreview
class FlowMultipleExamplesViewModel(
  private val weatherRepository: WeatherRepository,
  private val homeViewStateMapper: WeatherViewStateMapper
) : ViewModel() {

  //1
  val forecasts: LiveData<List<ForecastViewState>> = weatherRepository
    //2
    .getForecasts()
    //3
    .map {
      homeViewStateMapper.mapForecastsToViewState(it)
    }
    //4
    .asLiveData()

  val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

  private val _locations = queryChannel
    //1
    .asFlow()
    //2
    .debounce(SEARCH_DELAY_MILLIS)
    // other way of swithing between main and background thread in FLOW
    //.flowOn(Dispatchers.Main)
    .flowOn(Dispatchers.IO)
    //3
    .mapLatest {
      if (it.length >= MIN_QUERY_LENGTH) {
        getLocations(it)
      } else {
        emptyList()
      }
    }
    //4
    .catch {
      // Log Error
    }

  val locations = _locations.asLiveData()

  private suspend fun getLocations(query: String): List<LocationViewState> {
    val locations = viewModelScope.async { weatherRepository.findLocation(query) }

    return homeViewStateMapper.mapLocationsToViewState(locations.await())
  }

  fun fetchLocationDetails(cityId: Int) {
    viewModelScope.launch {
      weatherRepository.fetchLocationDetails(cityId)
    }
  }
}
