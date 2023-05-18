package com.books.gamewepapp.di

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val URL = "https://gamewebapp-010103-default-rtdb.europe-west1.firebasedatabase.app"

    @Singleton
    @Provides
    fun provideFirebaseDatabaseInstance(): DatabaseReference {
        return FirebaseDatabase.getInstance(URL).reference
    }
}