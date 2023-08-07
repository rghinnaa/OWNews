package com.akb.ownews.base

data class BaseResponse<T>(
    var articles: T? = null,
    var sources: T? = null,
    var status: Any? = null,
    var message: Any? = null
)
