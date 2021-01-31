package com.test.nikeapplication.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.test.nikeapplication.data.entities.Word
import com.test.nikeapplication.data.repository.WordRepository
import com.test.nikeapplication.utils.Resource
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor(
    private val repository: WordRepository
) : ViewModel() {

    val repoWordsFlow: Flow<Resource<List<Word>?>>
    private val repoWords: LiveData<Resource<List<Word>?>>
    private var filterWord = MutableLiveData<FilterEnum>().apply { value = FilterEnum.THUMBS_UP }
    val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    // exposed LiveData
    var orderedWords = MediatorLiveData<List<Word>>()
    val loading = MutableLiveData<Boolean>().apply { value = false }
    val feedBackMsg = MutableLiveData<Any>().apply { value = null }

    companion object {
        const val DELAY_TIME = 1100L
        const val ROUTINE_ERROR = com.test.nikeapplication.R.string.routine_error
        const val FLOW_ERROR = com.test.nikeapplication.R.string.flow_error
        const val IS_EMPTY = com.test.nikeapplication.R.string.empty_list
    }

    init {
        repoWordsFlow = queryChannel
            .asFlow()
            .debounce(DELAY_TIME)
            .mapLatest {
                try {
                    withContext(Dispatchers.IO) {
                        if (it.isNotEmpty()) {
                            loading.postValue(true);
                            val result = repository.getWordDefinition(it);
                            loading.postValue(false);
                            result

                        } else {
                            Resource.error(null)
                        }
                    }
                } catch (e: Throwable) {
                    if (e is CancellationException) {
                        throw e
                    } else {
                        Resource.error(e.message, messageID = ROUTINE_ERROR)
                    }
                }
            }.catch { emit(Resource.error(null, messageID = FLOW_ERROR)) }

        repoWords = repoWordsFlow.asLiveData()

        orderedWords.addSource(repoWords) {
            orderedWords.value = combineLatestData(repoWords.value, filterWord.value)
        }
        orderedWords.addSource(filterWord) {
            orderedWords.value = combineLatestData(repoWords.value, filterWord.value)
        }

    }

    fun combineLatestData(
        repoWordsValue: Resource<List<Word>?>?,
        filterValue: FilterEnum?
    ): List<Word> {

        if (repoWordsValue != null) {

            val value: Resource<List<Word>?> = repoWordsValue

            return if (value.status == Resource.Status.SUCCESS && value.data != null) {
                val data: List<Word> = value.data

                feedBackMsg.value = if (data.isEmpty()) IS_EMPTY else null

                when (filterValue!!) {
                    FilterEnum.THUMBS_UP -> data.sortedByDescending { it1 -> it1.thumbs_up }
                    FilterEnum.THUMBS_DOWN -> data.sortedByDescending { it1 -> it1.thumbs_down }
                }

            } else {
                feedBackMsg.value = value.message ?: value.messageID
                arrayListOf()
            }

        } else {
            return arrayListOf()
        }
    }


    fun setFilter(currentFilter: FilterEnum) {
        filterWord.value = currentFilter
    }

    fun getCurrentFilter(): FilterEnum = filterWord.value!!;
}