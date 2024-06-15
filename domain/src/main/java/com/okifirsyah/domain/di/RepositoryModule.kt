package com.okifirsyah.domain.di

import com.okifirsyah.domain.repository.AuthRepositoryImpl
import com.okifirsyah.domain.repository.StoryRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single { AuthRepositoryImpl(get()) }
    single { StoryRepositoryImpl(get()) }
}