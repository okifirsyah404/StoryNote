package com.okifirsyah.storynote.presentation.view.sign_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okifirsyah.data.network.ApiResponse
import com.okifirsyah.domain.repository.AuthRepositoryImpl
import kotlinx.coroutines.launch

class SignInViewModel(private val repository: AuthRepositoryImpl) : ViewModel() {

    val signInResult: LiveData<ApiResponse<String>> by lazy { _signInResult }
    private val _signInResult = MutableLiveData<ApiResponse<String>>()

    fun signIn(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            repository.signIn(email, password)
                .collect {
                    _signInResult.value = it
                }
        }
    }

}