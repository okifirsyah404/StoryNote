package com.okifirsyah.storynote.presentation.view.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.okifirsyah.data.local.preference.StoragePreference
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SettingViewModel(private val pref: StoragePreference) : ViewModel() {

    fun getThemeSettings(): LiveData<Boolean?> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun clearThemeSetting() {
        viewModelScope.launch {
            pref.clearThemeSetting()
        }
    }

    fun getLocaleSettings(): LiveData<String?> {
        return pref.getLocaleSetting().asLiveData()
    }

    fun saveLocaleSetting(locale: String) {
        viewModelScope.launch {
            pref.saveLocaleSetting(locale)
        }
    }

    fun deleteAccessToken() {
        runBlocking {
            pref.clearAccessToken()
        }
    }

}