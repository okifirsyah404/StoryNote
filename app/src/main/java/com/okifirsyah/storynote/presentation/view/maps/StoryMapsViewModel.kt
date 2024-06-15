package com.okifirsyah.storynote.presentation.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okifirsyah.data.dto.StoryDto
import com.okifirsyah.data.network.ApiResponse
import com.okifirsyah.domain.repository.StoryRepositoryImpl
import kotlinx.coroutines.launch

class StoryMapsViewModel(private val repository: StoryRepositoryImpl) : ViewModel() {

    val storiesResult: LiveData<ApiResponse<List<StoryDto>>> by lazy { _storiesResult }
    private val _storiesResult = MutableLiveData<ApiResponse<List<StoryDto>>>()

    fun getStories() {
        viewModelScope.launch {
            repository.getStories(1)
                .collect {
                    _storiesResult.value = it
                }
        }
    }
}