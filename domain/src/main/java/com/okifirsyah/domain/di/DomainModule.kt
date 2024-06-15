package com.okifirsyah.domain.di

import org.koin.dsl.module

val domainModule = module {
    includes(repositoryModule)
}