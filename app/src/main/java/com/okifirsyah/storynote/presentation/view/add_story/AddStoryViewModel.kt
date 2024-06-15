package com.okifirsyah.storynote.presentation.view.add_story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okifirsyah.data.network.ApiResponse
import com.okifirsyah.domain.repository.StoryRepositoryImpl
import kotlinx.coroutines.launch
import java.io.File

class AddStoryViewModel(private val repository: StoryRepositoryImpl) : ViewModel() {

    val addStoryResult: LiveData<ApiResponse<String>> by lazy { _addStoryResult }
    private val _addStoryResult = MutableLiveData<ApiResponse<String>>()

    fun addStory(
        image: File,
        description: String,
        latitude: Float?,
        longitude: Float?
    ) {
        viewModelScope.launch {
            repository.addStory(
                image = image,
                description = description,
                latitude = latitude,
                longitude = longitude
            )
                .collect {
                    _addStoryResult.value = it
                }
        }
    }

}