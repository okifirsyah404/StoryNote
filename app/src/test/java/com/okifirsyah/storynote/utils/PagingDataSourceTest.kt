package com.okifirsyah.storynote.utils

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.okifirsyah.data.local.model.StoryModel

class PagingDataSourceTest :
    PagingSource<Int, LiveData<List<StoryModel>>>() {

    companion object {
        fun snapshot(items: List<StoryModel>): PagingData<StoryModel> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryModel>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryModel>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

}