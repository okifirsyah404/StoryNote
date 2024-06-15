package com.okifirsyah.storynote.presentation.view.detail_story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okifirsyah.data.dto.StoryDto
import com.okifirsyah.data.network.ApiResponse
import com.okifirsyah.domain.repository.StoryRepositoryImpl
import kotlinx.coroutines.launch

class DetailStoryViewModel(private val repositoryImpl: StoryRepositoryImpl) : ViewModel() {

    val story: LiveData<ApiResponse<StoryDto>> by lazy { _story }
    private val _story = MutableLiveData<ApiResponse<StoryDto>>()

    fun getStory(storyId: String) {
        viewModelScope.launch {
            repositoryImpl.getStoryDetail(storyId)
                .collect {
                    _story.value = it
                }
        }
    }

}