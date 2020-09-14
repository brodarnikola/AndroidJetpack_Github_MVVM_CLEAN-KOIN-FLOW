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

package com.vjezba.androidjetpackgithub.viewmodels

import androidx.lifecycle.*
import com.vjezba.domain.repository.LanguagesRepository

/**
 * The ViewModel for [SavedLanguagesFragment].
 */
class LanguagesListViewModel internal constructor(
    languagesRepository: LanguagesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    /*val domainLanguages: LiveData<List<LanguagesDb>> = getSavedGrowZoneNumber().switchMap {
        if (it == NO_GROW_ZONE) {
            val d =languagesRepository.getDomainLanguages()
            print("Data is: " + d)
            languagesRepository.getDomainLanguages()
        } else {
            val d = languagesRepository.getDomainLanguages()
            print("Data is: " + d)

            languagesRepository.getDomainLanguages()
            //plantRepository.getPlantsWithGrowZoneNumber(it)
        }
    }*/

    val languages = languagesRepository.getLanguages()

    fun setGrowZoneNumber(num: Int) {
        savedStateHandle.set(GROW_ZONE_SAVED_STATE_KEY, num)
    }

    fun clearGrowZoneNumber() {
        savedStateHandle.set(GROW_ZONE_SAVED_STATE_KEY, NO_GROW_ZONE)
    }

    fun isFiltered() = getSavedGrowZoneNumber().value != NO_GROW_ZONE

    private fun getSavedGrowZoneNumber(): MutableLiveData<Int> {
        return savedStateHandle.getLiveData(GROW_ZONE_SAVED_STATE_KEY, NO_GROW_ZONE)
    }

    companion object {
        private const val NO_GROW_ZONE = -1
        private const val GROW_ZONE_SAVED_STATE_KEY = "GROW_ZONE_SAVED_STATE_KEY"
    }
}
