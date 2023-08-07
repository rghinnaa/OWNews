package com.akb.ownews.data.remote.source

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.akb.ownews.data.remote.api.ApiCallback
import com.akb.ownews.utils.flowResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

class MainRemoteDataSource(callback: ApiCallback) {
    private val apiCallback = callback

    fun requestTrending(country: String) = flowResponse {
        apiCallback.requestTrending(country = country)
    }

    fun requestHeadlinePaging(
        country: String,
        category: String,
        sources: String,
        search: String
    ) = Pager(
        config = PagingConfig(10)
    ) {
        HeadlinePagingSource(apiCallback, country, category, sources, search)
    }
        .flow
        .flowOn(Dispatchers.IO)
        .catch { throwable ->
            Log.e("Error", throwable.message.toString())
        }

    fun requestSource(country: String) = flowResponse {
        apiCallback.requestSources(country = country)
    }

}