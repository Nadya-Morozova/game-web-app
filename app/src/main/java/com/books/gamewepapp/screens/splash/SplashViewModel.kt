package com.books.gamewepapp.screens.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.books.gamewepapp.repository.FirebaseRemoteConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val remoteConfigRepository: FirebaseRemoteConfigRepository
) : ViewModel() {

    val isWebViewVisibleOnScreen = MutableLiveData<Boolean>()

    fun getValueVisibility() {
        viewModelScope.launch {
            delay(2000L)
            isWebViewVisibleOnScreen.value = remoteConfigRepository.getBooleanValueForVisibilityView()
        }
    }

}