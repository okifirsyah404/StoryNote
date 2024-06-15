package com.okifirsyah.data.dto

import com.okifirsyah.data.local.model.StoryModel
import com.okifirsyah.data.network.response.StoryResponse
import java.util.Date

data class StoryDto(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: Date,
    val latitude: Float?,
    val longitude: Float?
) {
    companion object {
        fun fromModel(model: StoryModel): StoryDto {
            return StoryDto(
                id = model.id,
                name = model.name,
                description = model.description,
                photoUrl = model.photoUrl,
                createdAt = Date(model.createdAt),
                latitude = model.latitude,
                longitude = model.longitude
            )
        }

        fun fromResponse(response: StoryResponse): StoryDto {
            return StoryDto(
                id = response.id,
                name = response.name,
                description = response.description,
                photoUrl = response.photoUrl,
                createdAt = response.createdAt,
                latitude = response.latitude,
                longitude = response.longitude
            )
        }
    }
}
