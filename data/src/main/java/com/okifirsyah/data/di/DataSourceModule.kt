package com.okifirsyah.data.di

import com.okifirsyah.data.source.AuthDataSource
import com.okifirsyah.data.source.StoryDataSource
import org.koin.dsl.module

val dataSourceModule = module {
    single { AuthDataSource(get(), get()) }
    single { StoryDataSource(get(), get()) }
}