package com.okifirsyah.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.okifirsyah.data.local.dao.RemoteKeysDao
import com.okifirsyah.data.local.dao.StoryDao
import com.okifirsyah.data.local.model.RemoteKeysModel
import com.okifirsyah.data.local.model.StoryModel

@Database(
    entities = [StoryModel::class, RemoteKeysModel::class],
    version = 1,
    exportSchema = false
)
abstract class StoryNoteDatabase : RoomDatabase() {
    abstract fun getStoryDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}