package com.okifirsyah.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.okifirsyah.data.dto.StoryDto
import com.okifirsyah.data.local.model.StoryModel
import com.okifirsyah.data.network.ApiResponse
import com.okifirsyah.data.source.StoryDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import java.io.File

class StoryRepositoryImpl(private val dataSource: StoryDataSource) : StoryRepository {
    override suspend fun getStories(location: Int?): Flow<ApiResponse<List<StoryDto>>> =
        dataSource.fetchStories(location).flowOn(Dispatchers.IO)

    override suspend fun getStoryDetail(id: String): Flow<ApiResponse<StoryDto>> =
        dataSource.fetchStoryDetail(id).flowOn(Dispatchers.IO)

    override suspend fun addStory(
        image: File,
        description: String,
        latitude: Float?,
        longitude: Float?
    ): Flow<ApiResponse<String>> =
        dataSource.createStory(
            image = image,
            description = description,
            latitude = latitude,
            longitude = longitude
        ).flowOn(Dispatchers.IO)

    override fun getPagingStories(): LiveData<PagingData<StoryModel>> =
        dataSource.fetchPagingStories()

}