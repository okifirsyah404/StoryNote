package com.okifirsyah.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.okifirsyah.data.network.response.StoryResponse

@Entity(tableName = "story")
data class StoryModel(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val description: String,
    @ColumnInfo("photo_url")
    val photoUrl: String,
    @ColumnInfo("created_at")
    val createdAt: Long,
    val latitude: Float?,
    val longitude: Float?
) {
    companion object {
        fun fromResponse(response: StoryResponse) = StoryModel(
            id = response.id,
            name = response.name,
            description = response.description,
            photoUrl = response.photoUrl,
            createdAt = response.createdAt.time,
            latitude = response.latitude,
            longitude = response.longitude
        )
    }
}