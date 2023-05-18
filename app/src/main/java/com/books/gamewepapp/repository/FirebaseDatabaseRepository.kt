package com.books.gamewepapp.repository

import com.books.gamewepapp.utils.awaitSingle
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class FirebaseDatabaseRepository @Inject constructor(
    private val databaseReference: DatabaseReference
) {

    suspend fun getPinterestUrl(): String? {
        var resultUrl: String? = null
        val reference = databaseReference.awaitSingle()?.children
        if (reference != null) {
            for (item in reference) {
                resultUrl = item.getValue<String>()
            }
        }
        return resultUrl
    }


}