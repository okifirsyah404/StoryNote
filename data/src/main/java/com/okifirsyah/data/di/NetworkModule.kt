package com.okifirsyah.data.di

import com.okifirsyah.data.local.preference.StoragePreference
import com.okifirsyah.data.network.HeaderInterceptor
import com.okifirsyah.data.utils.BaseUrlProvider
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        return@single OkHttpClient.Builder()
            .addInterceptor(
                getHeaderAppJsonInterceptor(get())
            )
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BaseUrlProvider.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }
}

private fun getHeaderAppJsonInterceptor(storagePreference: StoragePreference): Interceptor {
    val headers = HashMap<String, String>()
    headers["Content-Type"] = "application/json"

    return HeaderInterceptor(headers, storagePreference)
}