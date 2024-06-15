package com.okifirsyah.data.utils.extension

import com.okifirsyah.data.utils.constant.NetworkConstant.MULTIPART_FILE_NAME
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun File.toMultipart(): MultipartBody.Part {
    return MultipartBody.Part
        .createFormData(
            name = MULTIPART_FILE_NAME,
            filename = this.name,
            body = this.asRequestBody()
        )
}