package com.akb.ownews.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akb.ownews.data.local.entity.Bookmark
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM BOOKMARK_ENTITY ORDER BY id DESC")
    fun getBookmark(): Flow<List<Bookmark>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(favorite: Bookmark)

    @Query("DELETE FROM BOOKMARK_ENTITY WHERE id = :id")
    suspend fun deleteBookmark(id: Int?)

    @Query("SELECT * FROM BOOKMARK_ENTITY WHERE " +
            "title LIKE :query OR description LIKE :query " +
            "ORDER BY id DESC")
    fun searchBookmark(query: String): Flow<List<Bookmark>>

}