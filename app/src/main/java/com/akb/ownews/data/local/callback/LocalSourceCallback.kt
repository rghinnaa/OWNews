package com.akb.ownews.data.local.callback

import com.akb.ownews.data.local.entity.Bookmark
import kotlinx.coroutines.flow.Flow

interface LocalSourceCallback {

    fun getBookmark(): Flow<List<Bookmark>>

    suspend fun insertBookmark(bookmark: Bookmark)

    suspend fun deleteBookmark(id: Int?)

    fun searchBookmark(query: String): Flow<List<Bookmark>>

}