package com.books.gamewepapp.screens.web

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.books.gamewepapp.repository.FirebaseDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebViewModel @Inject constructor(
    private val firebaseDbRepo: FirebaseDatabaseRepository
) : ViewModel() {

    private val _urlLiveData = MutableLiveData<String>()
    val urlLiveData: LiveData<String> = _urlLiveData

    fun getPinterestUrl() {
        viewModelScope.launch {
            _urlLiveData.value = firebaseDbRepo.getPinterestUrl()
        }
    }

}