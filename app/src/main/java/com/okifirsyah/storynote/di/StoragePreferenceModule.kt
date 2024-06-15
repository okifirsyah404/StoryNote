package com.okifirsyah.storynote.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.okifirsyah.data.local.preference.StoragePreference
import org.koin.dsl.module

fun storagePreferenceModule(pref: DataStore<Preferences>) = module {
    single {
        pref
    }

    single {
        StoragePreference.getInstance(get())
    }
}