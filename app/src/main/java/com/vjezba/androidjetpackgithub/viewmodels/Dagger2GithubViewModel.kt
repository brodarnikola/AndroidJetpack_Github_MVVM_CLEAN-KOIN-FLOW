package com.vjezba.androidjetpackgithub.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Dagger2GithubViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dagger2 github Fragment"
    }
    val text: LiveData<String> = _text
}