package com.test.nikeapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.test.nikeapplication.data.entities.Word
import com.test.nikeapplication.data.remote.UrbanDictionaryRemoteDataSource
import com.test.nikeapplication.data.repository.WordRepository
import com.test.nikeapplication.ui.FilterEnum
import com.test.nikeapplication.ui.MainViewModel
import com.test.nikeapplication.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@FlowPreview
@ExperimentalCoroutinesApi
class MainViewModelTest() {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    private val mainDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var remoteSource: UrbanDictionaryRemoteDataSource

    @Mock
    private lateinit var mockRepo: WordRepository

    private lateinit var fakeRepo: FakeRepo

    private lateinit var subject: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(mainDispatcher)
        fakeRepo = FakeRepo(remoteSource)
        subject = MainViewModel(fakeRepo)

    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInstantSearch() = mainDispatcher.runBlockingTest {
        // GIVEN
        val expectedQueries = listOf("aa", "bb", "cc", "ddd")
        val actualSent = mutableListOf<String>()


        // start collecting flows in a new coroutine
        val collectParent = launch {

            subject.repoWordsFlow.launchIn(this)

            subject.queryChannel.asFlow().mapLatest { query ->
                actualSent.add(query)
            }.launchIn(this)
        }

        for (query in expectedQueries) {
            subject.queryChannel.send(query)
            advanceTimeBy(35)
        }

        // actually trigger the debounce delay
        advanceTimeBy(MainViewModel.DELAY_TIME)


        collectParent.cancel()

        // THEN
        assert(fakeRepo.actualQueries.size == 1) { "Only saw one search" }
        assert(actualSent == expectedQueries) { "all queries were sent, then debounced" }
    }

    @Test
    fun whenDataEmptySendMsg() {

        var isFeedback = subject.feedBackMsg.value
        Assert.assertNull(isFeedback)

        subject.combineLatestData(Resource.success(arrayListOf()), FilterEnum.THUMBS_UP)

        isFeedback = subject.feedBackMsg.value
        Assert.assertNotNull(isFeedback)

    }


}

class FakeRepo(remoteDataSource: UrbanDictionaryRemoteDataSource) :
    WordRepository(remoteDataSource) {
    val actualQueries = mutableListOf<String>()

    override suspend fun getWordDefinition(term: String): Resource<List<Word>?> {
        actualQueries.add(term)
        return Resource.success(listOf())
    }


}

