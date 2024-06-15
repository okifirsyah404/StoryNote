package com.okifirsyah.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.okifirsyah.data.local.model.StoryModel

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllStories(users: List<StoryModel>)

    @Query("SELECT * FROM story")
    fun getAllStories(): PagingSource<Int, StoryModel>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}