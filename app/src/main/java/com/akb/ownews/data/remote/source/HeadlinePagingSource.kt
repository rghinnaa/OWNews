package com.akb.ownews.data.remote.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.akb.ownews.data.model.ArticleModel
import com.akb.ownews.data.remote.api.ApiCallback

class HeadlinePagingSource(
    private val apiCallback: ApiCallback,
    private val country: String,
    private val category: String,
    private val sources: String,
    private val search: String
) : PagingSource<Int, ArticleModel>() {
    lateinit var data: List<ArticleModel>

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleModel> {
        return try {
            val nextPage = params.key ?: 0

            val response = apiCallback.requestNewestPaging(
                country = country,
                category = category,
                sources = sources,
                search = search,
                pageSize = 10,
                page = nextPage)
            data = response.body()?.articles.orEmpty()

            LoadResult.Page(
                data,
                if (nextPage == 0) null else nextPage - 1,
                if (data.isEmpty()) null else nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ArticleModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}