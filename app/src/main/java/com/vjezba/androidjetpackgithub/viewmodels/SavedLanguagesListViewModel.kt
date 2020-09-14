package com.vjezba.androidjetpackgithub.viewmodels

import androidx.lifecycle.ViewModel
import com.vjezba.domain.repository.SavedLanguagesRepository

class SavedLanguagesListViewModel internal constructor(
    savedLanguages: SavedLanguagesRepository
) : ViewModel() {
    val savedAndAllLanguages =
        savedLanguages.getSavedLanguages()
}
