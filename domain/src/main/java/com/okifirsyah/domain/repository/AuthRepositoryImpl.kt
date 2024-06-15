package com.okifirsyah.domain.repository

import com.okifirsyah.data.network.ApiResponse
import com.okifirsyah.data.source.AuthDataSource
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(private val dataSource: AuthDataSource) : AuthRepository {
    override suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): Flow<ApiResponse<String>> {
        return dataSource.signUp(name = name, email = email, password = password)
    }

    override suspend fun signIn(email: String, password: String): Flow<ApiResponse<String>> {
        return dataSource.signIn(email, password)
    }
}