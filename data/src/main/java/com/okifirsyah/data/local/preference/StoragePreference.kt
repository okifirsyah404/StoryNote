package com.okifirsyah.data.local.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class StoragePreference(private val dataStore: DataStore<Preferences>) {

    private val themeKey = booleanPreferencesKey("theme_setting")
    private val localeKey = stringPreferencesKey("locale_setting")
    private val accessTokenKey = stringPreferencesKey("access_token")

    fun getThemeSetting(): Flow<Boolean?> {
        return dataStore.data.map { preferences ->
            Timber.d("Theme setting retrieved: ${preferences[themeKey]}")
            preferences[themeKey]
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[themeKey] = isDarkModeActive
            Timber.d("Theme setting saved: $isDarkModeActive")
        }
    }

    suspend fun clearThemeSetting() {
        dataStore.edit { preferences ->
            preferences.remove(themeKey)
            Timber.d("Theme setting cleared")
        }
    }

    fun getLocaleSetting(): Flow<String?> {
        return dataStore.data.map { preferences ->
            Timber.d("Locale setting retrieved: ${preferences[localeKey]}")
            preferences[localeKey]
        }
    }

    suspend fun saveLocaleSetting(locale: String) {
        dataStore.edit { preferences ->
            preferences[localeKey] = locale
            Timber.d("Locale setting saved: $locale")
        }
    }

    suspend fun clearLocaleSetting() {
        dataStore.edit { preferences ->
            preferences.remove(localeKey)
            Timber.d("Locale setting cleared")
        }
    }

    suspend fun saveAccessToken(accessToken: String) {
        dataStore.edit { preferences ->
            preferences[accessTokenKey] = accessToken
            Timber.d("Access token saved: $accessToken")
        }
    }

    fun getAccessToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            Timber.d("Access token retrieved: ${preferences[accessTokenKey]}")
            preferences[accessTokenKey]
        }
    }

    suspend fun clearAccessToken() {
        dataStore.edit { preferences ->
            preferences.remove(accessTokenKey)
            Timber.d("Access token cleared")
        }
    }

    companion object {
        private var INSTANCE: StoragePreference? = null
        fun getInstance(dataStore: DataStore<Preferences>): StoragePreference {
            return INSTANCE ?: synchronized(this) {
                val instance = StoragePreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}