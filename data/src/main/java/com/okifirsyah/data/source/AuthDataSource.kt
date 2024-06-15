package com.okifirsyah.data.source

import com.okifirsyah.data.local.preference.StoragePreference
import com.okifirsyah.data.network.ApiResponse
import com.okifirsyah.data.network.request.SignInRequest
import com.okifirsyah.data.network.request.SignUpRequest
import com.okifirsyah.data.network.service.AuthService
import com.okifirsyah.data.utils.extension.createResponse
import com.okifirsyah.data.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthDataSource(
    private val service: AuthService, private val pref: StoragePreference
) {

    suspend fun signUp(
        name: String,
        email: String,
        password: String,
    ): Flow<ApiResponse<String>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = service.signUp(
                    SignUpRequest(name, email, password)
                )
                if (response.error) {
                    emit(ApiResponse.Error(response.message))
                    return@flow
                }
                emit(ApiResponse.Success(response.message))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.createResponse()?.message ?: ""))
            }
        }
    }

    suspend fun signIn(
        email: String,
        password: String,
    ): Flow<ApiResponse<String>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                wrapEspressoIdlingResource {
                    val response = service.signIn(
                        SignInRequest(email, password)
                    )
                    if (response.error) {
                        emit(ApiResponse.Error(response.message))
                        return@flow
                    }

                    pref.saveAccessToken(response.data.token)
                    emit(ApiResponse.Success(response.message))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.createResponse()?.message ?: ""))
            }
        }
    }

}