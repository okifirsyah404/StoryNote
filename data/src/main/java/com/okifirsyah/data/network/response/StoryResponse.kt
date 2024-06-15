package com.okifirsyah.data.network.response

import com.google.gson.annotations.SerializedName
import java.util.Date

data class StoryResponse(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: Date,
    @SerializedName("lat")
    val latitude: Float?,
    @SerializedName("lon")
    val longitude: Float?
)
