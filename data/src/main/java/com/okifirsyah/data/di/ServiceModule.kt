package com.okifirsyah.data.di

import com.okifirsyah.data.network.service.AuthService
import com.okifirsyah.data.network.service.StoryService
import org.koin.dsl.module
import retrofit2.Retrofit

val serviceModule = module {
    single {
        provideAuthService(get())
    }
    single {
        provideStoryService(get())
    }
}

private fun provideAuthService(retrofit: Retrofit): AuthService {
    return retrofit.create(AuthService::class.java)
}

private fun provideStoryService(retrofit: Retrofit): StoryService {
    return retrofit.create(StoryService::class.java)
}