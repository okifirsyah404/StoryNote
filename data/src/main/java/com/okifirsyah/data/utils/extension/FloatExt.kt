package com.okifirsyah.data.utils.extension

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

fun Float?.toRequestBody(): RequestBody? {
    if (this == null) return null
    return this.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
}