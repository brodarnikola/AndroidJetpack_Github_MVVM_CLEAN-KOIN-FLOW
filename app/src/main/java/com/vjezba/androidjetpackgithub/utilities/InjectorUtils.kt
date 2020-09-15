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

package com.vjezba.androidjetpackgithub.utilities

import android.content.Context
import androidx.fragment.app.Fragment
import com.vjezba.androidjetpackgithub.viewmodels.LanguagesListViewModelFactory
import com.vjezba.androidjetpackgithub.viewmodels.LanguageDetailsViewModelFactory
import com.vjezba.androidjetpackgithub.viewmodels.SavedLanguagesListViewModelFactory
import com.vjezba.data.database.AppDatabase
import com.vjezba.data.database.mapper.DbMapperImpl
import com.vjezba.data.repository.LanguagesRepositoryImpl
import com.vjezba.data.repository.SavedLanguagesRepositoryImpl
import com.vjezba.domain.repository.LanguagesRepository
import com.vjezba.domain.repository.SavedLanguagesRepository

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    private fun getSavedLanguagesRepository(context: Context): SavedLanguagesRepository {
        val dbMapper = DbMapperImpl()
        return SavedLanguagesRepositoryImpl.getInstance(
            AppDatabase.getInstance(context.applicationContext).savedLanguagesDAO(), dbMapper
        )
    }

    fun provideSavedLanguagesViewModelFactory(context: Context) : SavedLanguagesListViewModelFactory {
        return SavedLanguagesListViewModelFactory(getSavedLanguagesRepository(context = context))
    }

   private fun getLanguagesRepository(context: Context): LanguagesRepository {
        val dbMapper = DbMapperImpl()
        return LanguagesRepositoryImpl.getInstance(
            AppDatabase.getInstance(context.applicationContext).languagesDAO(), dbMapper
        )
    }

    fun provideLanguagesListViewModelFactory(fragment: Fragment): LanguagesListViewModelFactory {
       return LanguagesListViewModelFactory(getLanguagesRepository(fragment.requireContext()), fragment)
   }


    fun provideLanguageDetailsViewModelFactory(
        context: Context,
        languagesId: String
    ): LanguageDetailsViewModelFactory {
        return LanguageDetailsViewModelFactory(
            getLanguagesRepository(context),
            getSavedLanguagesRepository(context),
            languagesId
        )
    }

}
