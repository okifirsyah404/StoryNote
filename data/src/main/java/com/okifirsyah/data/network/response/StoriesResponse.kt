package com.okifirsyah.data.network.response

import com.google.gson.annotations.SerializedName

data class StoriesResponse(
    val error: Boolean,
    val message: String,
    @SerializedName("listStory")
    val data: List<StoryResponse>
)
