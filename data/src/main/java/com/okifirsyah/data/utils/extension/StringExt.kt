package com.okifirsyah.data.utils.extension

import com.okifirsyah.data.utils.constant.NetworkConstant.MULTIPART_FORM_DATA
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

fun String.toRequestBody(): RequestBody {
    return this.toRequestBody(MULTIPART_FORM_DATA.toMediaTypeOrNull())
}