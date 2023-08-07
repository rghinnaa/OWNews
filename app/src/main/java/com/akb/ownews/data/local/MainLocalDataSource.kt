package com.akb.ownews.data.local

import com.akb.ownews.data.local.dao.BookmarkDao
import com.akb.ownews.data.local.entity.Bookmark

class MainLocalDataSource(bookmarkDao: BookmarkDao) {

    private val dao = bookmarkDao

    fun getBookmark() = dao.getBookmark()

    suspend fun insertBookmark(bookmark: Bookmark) {
        dao.insertBookmark(bookmark)
    }

    suspend fun deleteBookmark(id: Int?) {
        dao.deleteBookmark(id)
    }

    fun searchBookmark(query: String) = dao.searchBookmark(query)

}