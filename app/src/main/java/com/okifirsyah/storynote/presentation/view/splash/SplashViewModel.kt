package com.okifirsyah.storynote.presentation.view.splash

import androidx.lifecycle.ViewModel
import com.okifirsyah.data.local.preference.StoragePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SplashViewModel(private val preference: StoragePreference) : ViewModel() {
    fun isAlreadyLogin(): Flow<Boolean?> {
        val accessToken: Flow<String?> = preference.getAccessToken()

        return accessToken.map { token ->
            token != null
        }
    }
}