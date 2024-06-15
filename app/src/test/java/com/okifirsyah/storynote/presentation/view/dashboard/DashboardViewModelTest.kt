package com.okifirsyah.storynote.presentation.view.dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.okifirsyah.data.local.model.StoryModel
import com.okifirsyah.domain.repository.StoryRepositoryImpl
import com.okifirsyah.storynote.presentation.adapter.StoryPagingAdapter
import com.okifirsyah.storynote.utils.DummyData
import com.okifirsyah.storynote.utils.MainDispatcherRule
import com.okifirsyah.storynote.utils.PagingDataSourceTest
import com.okifirsyah.storynote.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DashboardViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepositoryImpl

    private lateinit var dashboardViewModel: DashboardViewModel

    @Before
    fun setup() {
        storyRepository = mock(StoryRepositoryImpl::class.java)
        dashboardViewModel = DashboardViewModel(storyRepository)
    }

    @Test
    fun `Stories Paging Should Not Null`() = runTest {
        val dummyStories = DummyData.generateStoryDummy()
        val data = PagingDataSourceTest.snapshot(dummyStories)

        val dataStories = MutableLiveData<PagingData<StoryModel>>()
        dataStories.value = data

        Mockito.`when`(storyRepository.getPagingStories()).thenReturn(dataStories)

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryPagingAdapter.DIFF_CALLBACK,
            updateCallback = listUpdateCallback,
            mainDispatcher = mainDispatcherRules.testDispatcher,
            workerDispatcher = mainDispatcherRules.testDispatcher
        )

        val result = dashboardViewModel.getPagingStories().getOrAwaitValue()


        differ.submitData(result)
        advanceUntilIdle()

        Assert.assertEquals(dummyStories.size, differ.snapshot().size)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStories.size, differ.snapshot().size)
        Assert.assertEquals(dummyStories[0], differ.snapshot()[0])
    }

    @Test
    fun `Stories length should be the same`() = runTest {
        val dataDummyStories = DummyData.generateStoryDummy()
        val data = PagingDataSourceTest.snapshot(dataDummyStories)

        val dataStories = MutableLiveData<PagingData<StoryModel>>()
        dataStories.value = data

        Mockito.`when`(storyRepository.getPagingStories()).thenReturn(dataStories)

        val result = dashboardViewModel.getPagingStories().getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryPagingAdapter.DIFF_CALLBACK,
            updateCallback = listUpdateCallback,
            mainDispatcher = mainDispatcherRules.testDispatcher,
            workerDispatcher = mainDispatcherRules.testDispatcher
        )
        differ.submitData(result)
        advanceUntilIdle()

        Assert.assertEquals(dataDummyStories.size, differ.snapshot().size)
    }

    @Test
    fun `First Story item should be the same`() = runTest {
        val dataDummyStories = DummyData.generateStoryDummy()
        val data = PagingDataSourceTest.snapshot(dataDummyStories)

        val dataStories = MutableLiveData<PagingData<StoryModel>>()
        dataStories.value = data

        Mockito.`when`(storyRepository.getPagingStories()).thenReturn(dataStories)

        val result = dashboardViewModel.getPagingStories().getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryPagingAdapter.DIFF_CALLBACK,
            updateCallback = listUpdateCallback,
            mainDispatcher = mainDispatcherRules.testDispatcher,
            workerDispatcher = mainDispatcherRules.testDispatcher
        )
        differ.submitData(result)
        advanceUntilIdle()

        Assert.assertEquals(dataDummyStories[0], differ.snapshot()[0])
    }

    @Test
    fun `If Story empty, should return 0`() = runTest {
        val dataDummyStories = DummyData.emptyListStoryDummy()
        val data = PagingDataSourceTest.snapshot(dataDummyStories)

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryPagingAdapter.DIFF_CALLBACK,
            updateCallback = listUpdateCallback,
            mainDispatcher = mainDispatcherRules.testDispatcher,
            workerDispatcher = mainDispatcherRules.testDispatcher
        )
        differ.submitData(data)
        advanceUntilIdle()

        Assert.assertEquals(differ.snapshot().size, 0)
    }

    private val listUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}

