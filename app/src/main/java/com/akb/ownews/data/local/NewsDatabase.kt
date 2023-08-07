package com.akb.ownews.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.akb.ownews.data.local.dao.BookmarkDao
import com.akb.ownews.data.local.entity.Bookmark

@Database(
    entities = [Bookmark::class],
    version = 2,
    exportSchema = false
)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun bookmarkDao(): BookmarkDao

}