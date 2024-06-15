package com.okifirsyah.data.network

import com.okifirsyah.data.local.preference.StoragePreference
import com.okifirsyah.data.utils.constant.LogKeyConstant.HEADER_INTERCEPTOR
import com.okifirsyah.data.utils.constant.NetworkConstant.AUTHORIZATION
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber

class HeaderInterceptor(
    private val requestHeaders: HashMap<String, String>,
    private val storagePref: StoragePreference
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val path = chain.request().url.toString()
        Timber.tag(HEADER_INTERCEPTOR).d("<<<<<<<<< $path")

        if (path.contains("logout")) {
            mapRequestHeaders()
            Timber.tag(HEADER_INTERCEPTOR).d("<<<<<<<<< $requestHeaders")
        } else {
            mapRequestHeaders()
        }

        val request = mapHeaders(chain)

        return chain.proceed(request)
    }

    private fun mapRequestHeaders() {
        Timber.tag(HEADER_INTERCEPTOR).d("<<<<<<<<< Before : $requestHeaders")

        val token = runBlocking {
            storagePref.getAccessToken().first()
        } ?: ""

        requestHeaders[AUTHORIZATION] = "Bearer $token"
        Timber.tag(HEADER_INTERCEPTOR).d("Bearer Token : $token")

        Timber.tag(HEADER_INTERCEPTOR).d("<<<<<<<<< After : $requestHeaders")
    }


    private fun mapHeaders(chain: Interceptor.Chain): Request {
        val original = chain.request()

        val requestBuilder = original.newBuilder()

        for ((key, value) in requestHeaders) {
            requestBuilder.addHeader(key, value)
        }
        return requestBuilder.build()
    }
}
