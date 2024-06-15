package com.okifirsyah.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.okifirsyah.data.dto.StoryDto
import com.okifirsyah.data.local.model.StoryModel
import com.okifirsyah.data.network.ApiResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface StoryRepository {

    suspend fun getStories(location: Int?): Flow<ApiResponse<List<StoryDto>>>

    suspend fun getStoryDetail(id: String): Flow<ApiResponse<StoryDto>>

    suspend fun addStory(
        image: File,
        description: String,
        latitude: Float?,
        longitude: Float?
    ): Flow<ApiResponse<String>>

    fun getPagingStories(): LiveData<PagingData<StoryModel>>

}