package com.test.nikeapplication.data.remote

import com.test.nikeapplication.data.entities.WordList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UrbanDictionaryService {
    @GET("define")
    suspend fun getWords(@Query("term") term: String): Response<WordList>
}