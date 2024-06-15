package com.okifirsyah.storynote.presentation.view.sign_up

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okifirsyah.data.network.ApiResponse
import com.okifirsyah.domain.repository.AuthRepositoryImpl
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: AuthRepositoryImpl) : ViewModel() {


    val signUpResult: LiveData<ApiResponse<String>> by lazy { _signUpResult }
    private val _signUpResult = MutableLiveData<ApiResponse<String>>()

    fun signUp(
        email: String,
        password: String,
        name: String,
    ) {
        viewModelScope.launch {
            repository.signUp(name = name, email = email, password = password)
                .collect {
                    _signUpResult.value = it
                }
        }
    }

}