package com.test.nikeapplication.data.remote

import com.test.nikeapplication.data.entities.Word
import com.test.nikeapplication.utils.Resource
import javax.inject.Inject


class UrbanDictionaryRemoteDataSource @Inject constructor(
    private val urbanDictionaryService: UrbanDictionaryService
) : BaseRemoteDataSource() {

    suspend fun getWords(term: String): Resource<List<Word>?> {
        val result = getResult { urbanDictionaryService.getWords(term) }
        return Resource(result.status, result.data?.list, result.message, result.messageID)
    }
}