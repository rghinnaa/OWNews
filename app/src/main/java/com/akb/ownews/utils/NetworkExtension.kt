package com.akb.ownews.utils

import android.util.Log
import com.akb.ownews.base.BaseResponse
import com.akb.ownews.data.remote.Result
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retryWhen
import okhttp3.ResponseBody
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

private fun Throwable.handleException() = when (this) {
    is IOException -> "Failed to read response!"
    is SocketTimeoutException -> "Timeout!"
    is UnknownHostException -> "Check your internet connection!"
    else -> "An error occurred!"
}

fun Int.handleCode() = when (this) {
    401 -> "API KEY tidak sesuai"
    403 -> "Silahkan masukkan API KEY"
    404 -> "Halaman yang dituju tidak dapat ditemukan, harap mencoba ulang secara berkala"
    422 -> "Data yang diinputkan tidak dapat diproses, harap coba input data yang berbeda atau tunggu beberapa saat"
    else -> "Mohon maaf terjadi kesalahan, tunggu beberapa saat untuk mencoba kembali"
}

val Throwable.parsedMessage get() = handleException()

inline fun<reified T: Any?> ResponseBody.parse(): T? {
    val classType= object: TypeToken<T>(){}.type
    return Gson().fromJson(charStream(), classType)
}

inline fun Gson.toJsonElement(source: HashMap<String, Any>.() -> Unit): JsonElement {
    val hashMap = HashMap<String, Any>()
    hashMap.source()

    return toJsonTree(hashMap)
}

inline fun <reified T> flowResponse(
    handleError: Boolean = true,
    crossinline errorMessage: (String) -> String = { "" },
    crossinline responseBody: suspend () -> Response<BaseResponse<T>>
) = flow<Result<T>> {
    val response = responseBody.invoke()
    val body = response.body()

    if (response.isSuccessful) {
        val result = if(body?.articles != null) body.articles
        else body?.sources

        val status = body?.status.toString()
        val message = body?.message.toString()
        emit(Result.success(status, message, result))

    } else {
        val isError = response.code() in 400..599
        val errorBody = response.errorBody()?.parse<BaseResponse<T>>()

        val message =
            if (isError && handleError) response.code().handleCode()
            else if (errorMessage.invoke(errorBody.toString()) != "") errorMessage.invoke(errorBody.toString())
            else errorBody.toString()

        emit(
            Result.error(message, errorBody?.articles)
        )
    }
}.onStart { emit(Result.loading()) }
    .flowOn(Dispatchers.IO)
    .retryWhen { cause, attempt ->
        attempt <= 3 && cause is SocketTimeoutException
    }
    .catch { throwable ->
        Log.e("Error", "Error @${T::class.java} : $throwable")
        emit(Result.error<T>(throwable.parsedMessage, null))
    }