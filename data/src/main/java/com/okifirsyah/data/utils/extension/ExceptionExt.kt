package com.okifirsyah.data.utils.extension

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.okifirsyah.data.base.BaseResponse
import retrofit2.HttpException
import java.io.IOException
import java.lang.reflect.Type

fun Exception.createResponse(): BaseResponse? {

    val gson = Gson()
    val type: Type = object : TypeToken<BaseResponse>() {}.type

    return when (this) {
        is HttpException -> {
            gson.fromJson(response()?.errorBody()?.charStream(), type)
        }

        else -> {
            BaseResponse(
                message = this.message ?: "",
                error = true
            )
        }
    }
}

fun Exception.createErrorResponse(): String {
    return when (this) {
        is IOException -> "Network error occurred"
        is HttpException -> {
            val errorBody = response()?.errorBody()?.string()
            errorBody ?: "HTTP error occurred"
        }

        else -> "Unknown error occurred"
    }
}