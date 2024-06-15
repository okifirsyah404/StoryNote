package com.okifirsyah.data.network.service

import com.okifirsyah.data.network.request.SignInRequest
import com.okifirsyah.data.network.request.SignUpRequest
import com.okifirsyah.data.network.response.SignInResponse
import com.okifirsyah.data.network.response.SignUpResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("register")
    suspend fun signUp(
        @Body request: SignUpRequest
    ): SignUpResponse

    @POST("login")
    suspend fun signIn(
        @Body request: SignInRequest
    ): SignInResponse
}