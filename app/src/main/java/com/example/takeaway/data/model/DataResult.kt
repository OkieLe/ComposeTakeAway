package com.example.takeaway.data.model

import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.lang.Exception

sealed class DataResult<T> {
    open fun get(): T? = null

    fun isSuccess(): Boolean = this is Success
}

data class Success<T>(val data: T) : DataResult<T>()

data class Error<T>(val throwable: Throwable, val isNetworkError: Boolean = false) : DataResult<T>()

inline fun <T> wrapDataResult(dataFetcher: () -> T): DataResult<T> {
    return try {
        Success(dataFetcher.invoke())
    } catch (e: IOException) {
        Timber.d("Network error ${e.message}")
        Error(e, true)
    } catch (e: HttpException) {
        Timber.d("Server error ${e.message}")
        Error(e)
    } catch (e: Exception) {
        Timber.d("Unknown error ${e.message}")
        Error(e)
    }
}
