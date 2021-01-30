package com.test.nikeapplication.data.repository

import com.test.nikeapplication.data.entities.Word
import com.test.nikeapplication.data.remote.UrbanDictionaryRemoteDataSource
import com.test.nikeapplication.utils.Resource
import javax.inject.Inject

class WordRepository @Inject constructor(
    private val remoteDataSource: UrbanDictionaryRemoteDataSource,
) {

//    fun getWordDefinition(term: String): Resource<WordList> =
//        liveData(Dispatchers.IO) {
//            emit(Resource.loading())
//
//            val responseStatus =
//            if (responseStatus.status == Resource.Status.SUCCESS) {
//
//                emit(responseStatus)
//            } else if (responseStatus.status == Resource.Status.ERROR) {
//                emit(Resource.error(responseStatus.message!!))
//                //  emitSource(source)
//            }
//        }

    suspend fun getWordDefinition(term: String): Resource<List<Word>?> {
        return remoteDataSource.getWords(term)
    }


}