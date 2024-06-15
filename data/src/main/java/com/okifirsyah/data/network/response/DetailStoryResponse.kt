package com.okifirsyah.data.network.response

import com.google.gson.annotations.SerializedName

data class DetailStoryResponse(
    val error: Boolean,
    val message: String,
    @SerializedName("story")
    val data: StoryResponse
)