package com.okifirsyah.storynote.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.okifirsyah.data.local.preference.StoragePreference

class MainActivityViewModel(private val pref: StoragePreference) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean?> {
        return pref.getThemeSetting().asLiveData()
    }

    suspend fun setLocaleSettings(locale: String) {
        pref.saveLocaleSetting(locale)
    }

    fun getLocaleSettings(): LiveData<String?> {
        return pref.getLocaleSetting().asLiveData()
    }
}