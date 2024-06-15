package com.okifirsyah.data.source

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.okifirsyah.data.dto.StoryDto
import com.okifirsyah.data.local.model.StoryModel
import com.okifirsyah.data.local.room.StoryNoteDatabase
import com.okifirsyah.data.mediator.StoryRemoteMediator
import com.okifirsyah.data.network.ApiResponse
import com.okifirsyah.data.network.service.StoryService
import com.okifirsyah.data.utils.extension.createResponse
import com.okifirsyah.data.utils.extension.toMultipart
import com.okifirsyah.data.utils.extension.toRequestBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class StoryDataSource(private val service: StoryService, private val database: StoryNoteDatabase) {

    suspend fun fetchStories(location: Int?): Flow<ApiResponse<List<StoryDto>>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = service.fetchStories(location)
                if (response.error) {
                    emit(ApiResponse.Error(response.message))
                    return@flow
                }

                if (response.data.isEmpty()) {
                    emit(ApiResponse.Empty)
                    return@flow
                }

                val story = response.data.map {
                    StoryDto.fromResponse(it)
                }

                emit(ApiResponse.Success(story))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.createResponse()?.message ?: ""))
            }
        }
    }

    fun fetchPagingStories(): LiveData<PagingData<StoryModel>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 7,
            ),
            remoteMediator = StoryRemoteMediator(database, service),
            pagingSourceFactory = {
                database.getStoryDao().getAllStories()
            }
        ).liveData
    }

    suspend fun fetchStoryDetail(id: String): Flow<ApiResponse<StoryDto>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = service.fetchStoryDetail(id)
                if (response.error) {
                    emit(ApiResponse.Error(response.message))
                    return@flow
                }

                emit(ApiResponse.Success(StoryDto.fromResponse(response.data)))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.createResponse()?.message ?: ""))
            }
        }
    }

    suspend fun createStory(
        description: String,
        image: File,
        latitude: Float?,
        longitude: Float?
    ): Flow<ApiResponse<String>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = service.addStory(
                    description = description.toRequestBody(),
                    photo = image.toMultipart(),
                    latitude = latitude.toRequestBody(),
                    longitude = longitude.toRequestBody()
                )

                if (response.error) {
                    emit(ApiResponse.Error(response.message))
                    return@flow
                }

                emit(ApiResponse.Success(response.message))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.createResponse()?.message ?: ""))
            }
        }
    }

}