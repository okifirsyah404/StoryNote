package com.okifirsyah.data.di

import android.app.Application
import androidx.room.Room
import com.okifirsyah.data.BuildConfig
import com.okifirsyah.data.local.room.StoryNoteDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module


val databaseModule = module {

    factory { get<StoryNoteDatabase>().getStoryDao() }
    factory { get<StoryNoteDatabase>().remoteKeysDao() }

    single { provideDatabase(androidApplication()) }
}

fun provideDatabase(application: Application): StoryNoteDatabase {
    return Room.databaseBuilder(application, StoryNoteDatabase::class.java, BuildConfig.DB_NAME)
        .fallbackToDestructiveMigration()
        .build()
}