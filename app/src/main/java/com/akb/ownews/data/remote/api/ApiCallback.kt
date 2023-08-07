package com.akb.ownews.data.remote.api

import com.akb.ownews.base.BaseResponse
import com.akb.ownews.data.model.ArticleModel
import com.akb.ownews.data.model.SourceModel
import com.akb.ownews.utils.Const
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiCallback {

    @GET(Const.NEWS_TOP_HEADLINE)
    suspend fun requestTrending(
        @Query("apiKey") apiKey: String = Const.API_KEY,
        @Query("sortBy") sortBy: String = Const.SortBy.POPULARITY,
        @Query("country") country: String,
        @Query("pageSize") pageSize: Int = 1,
        @Query("page") page: Int = 2
    ): Response<BaseResponse<List<ArticleModel>>>

    @GET(Const.NEWS_TOP_HEADLINE)
    suspend fun requestNewestPaging(
        @Query("apiKey") apiKey: String = Const.API_KEY,
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("sources") sources: String,
        @Query("q") search: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int
    ): Response<BaseResponse<List<ArticleModel>>>

   @GET(Const.NEWS_SOURCE)
   suspend fun requestSources(
       @Query("apiKey") apiKey: String = Const.API_KEY,
       @Query("country") country: String
   ): Response<BaseResponse<List<SourceModel>>>

}