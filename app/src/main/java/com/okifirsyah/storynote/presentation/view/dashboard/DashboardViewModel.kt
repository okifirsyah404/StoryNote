package com.okifirsyah.storynote.presentation.view.dashboard

import androidx.lifecycle.ViewModel
import com.okifirsyah.domain.repository.StoryRepositoryImpl

class DashboardViewModel(private val repository: StoryRepositoryImpl) : ViewModel() {

//    val storiesResult: LiveData<ApiResponse<List<StoryDto>>> by lazy { _storiesResult }
//    private val _storiesResult = MutableLiveData<ApiResponse<List<StoryDto>>>()
//
//    fun getStories() {
//        viewModelScope.launch {
//            repository.getStories(null)
//                .collect {
//                    _storiesResult.value = it
//                }
//        }
//    }

    fun getPagingStories() = repository.getPagingStories()


}