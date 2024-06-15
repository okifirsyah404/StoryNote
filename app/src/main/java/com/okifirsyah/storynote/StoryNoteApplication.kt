package com.okifirsyah.storynote

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.okifirsyah.data.di.dataModule
import com.okifirsyah.domain.di.domainModule
import com.okifirsyah.storynote.di.storagePreferenceModule
import com.okifirsyah.storynote.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class StoryNoteApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@StoryNoteApplication)

            modules(
                listOf(
                    storagePreferenceModule(dataStore),
                    dataModule,
                    domainModule,
                    viewModelModule,
                )
            )
        }
    }

    companion object {
        val Context.dataStore by preferencesDataStore(name = "story_note_settings")
    }

}