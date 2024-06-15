package com.okifirsyah.domain.repository

import com.okifirsyah.data.network.ApiResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): Flow<ApiResponse<String>>

    suspend fun signIn(
        email: String,
        password: String
    ): Flow<ApiResponse<String>>

}