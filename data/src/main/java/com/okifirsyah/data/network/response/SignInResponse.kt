package com.okifirsyah.data.network.response

import com.google.gson.annotations.SerializedName

data class SignInResponse(
    val error: Boolean,
    val message: String,
    @SerializedName("loginResult")
    val data: SignInResultResponse
)
