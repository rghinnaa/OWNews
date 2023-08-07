package com.akb.ownews.data.remote.source

import androidx.paging.PagingData
import com.akb.ownews.data.model.ArticleModel
import com.akb.ownews.data.model.SourceModel
import com.akb.ownews.data.remote.Result
import kotlinx.coroutines.flow.Flow

interface MainSourceCallback {

    fun requestTrending(country: String): Flow<Result<List<ArticleModel>>>

    fun requestHeadlinePaging(
        country: String,
        category: String,
        sources: String,
        search: String
    ): Flow<PagingData<ArticleModel>>

    fun requestSource(country: String): Flow<Result<List<SourceModel>>>

}