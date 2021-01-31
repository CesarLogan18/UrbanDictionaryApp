package com.test.nikeapplication.data.repository

import com.test.nikeapplication.data.entities.Word
import com.test.nikeapplication.data.remote.UrbanDictionaryRemoteDataSource
import com.test.nikeapplication.utils.Resource
import javax.inject.Inject

open class WordRepository @Inject constructor(
    private val remoteDataSource: UrbanDictionaryRemoteDataSource,
) {

    open suspend fun getWordDefinition(term: String): Resource<List<Word>?> {
        return remoteDataSource.getWords(term)
    }


}