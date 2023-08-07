package com.akb.ownews.data.remote.repository

import com.akb.ownews.data.local.MainLocalDataSource
import com.akb.ownews.data.local.callback.LocalSourceCallback
import com.akb.ownews.data.local.entity.Bookmark
import com.akb.ownews.data.model.SourceModel
import com.akb.ownews.data.remote.source.MainRemoteDataSource
import com.akb.ownews.data.remote.source.MainSourceCallback
import kotlinx.coroutines.flow.Flow

class MainRepository(
    mainRemoteDataSource: MainRemoteDataSource,
    mainLocalDataSource: MainLocalDataSource
) : MainSourceCallback, LocalSourceCallback {
    private val remoteDataSource = mainRemoteDataSource
    private val localDataSource = mainLocalDataSource

    override fun requestTrending(country: String) = remoteDataSource.requestTrending(country)

    override fun requestHeadlinePaging(
        country: String,
        category: String,
        sources: String,
        search: String
    ) = remoteDataSource.requestHeadlinePaging(country, category, sources, search)

    override fun getBookmark() = localDataSource.getBookmark()

    override suspend fun insertBookmark(bookmark: Bookmark) =
        localDataSource.insertBookmark(bookmark)

    override suspend fun deleteBookmark(id: Int?) = localDataSource.deleteBookmark(id)

    override fun searchBookmark(queryString: String) = localDataSource.searchBookmark(queryString)

    override fun requestSource(country: String) = remoteDataSource.requestSource(country)

}