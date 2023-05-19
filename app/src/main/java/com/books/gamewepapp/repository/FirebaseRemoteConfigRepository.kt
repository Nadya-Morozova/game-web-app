package com.books.gamewepapp.repository

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@ViewModelScoped
class FirebaseRemoteConfigRepository @Inject constructor(
    private val instanceOfRemoteConfig: FirebaseRemoteConfig
) {

    suspend fun getBooleanValueForVisibilityView(): Boolean {
        var result = true
        instanceOfRemoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val value = instanceOfRemoteConfig.getBoolean("isWebViewVisibleOnScreen")
                result = value
            }
        }.await()
        return result
    }

}