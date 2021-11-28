package com.example.takeaway.data.model

import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.lang.Exception
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed class DataResult<T>

data class Success<T>(val data: T) : DataResult<T>()
data class Error<T>(val type: ErrorType, val message: String? = "") : DataResult<T>()

sealed interface ErrorType {
    object NetworkError : ErrorType
    object ServerError : ErrorType
}

@OptIn(ExperimentalContracts::class)
fun <T> DataResult<T>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is Success)
    }
    return this is Success
}

fun <T> DataResult<T>.onSuccess(block: (T) -> Unit): DataResult<T> {
    if (isSuccess()) {
        block(data)
    }
    return this
}

@OptIn(ExperimentalContracts::class)
fun <T> DataResult<T>.isError(): Boolean {
    contract {
        returns(true) implies (this@isError is Error)
    }
    return this is Error
}

fun <T> DataResult<T>.onError(block: (ErrorType, String?) -> Unit): DataResult<T> {
    if (isError()) {
        block(type, message)
    }
    return this
}

inline fun <T> wrapDataResult(dataFetcher: () -> T): DataResult<T> {
    return try {
        Success(dataFetcher.invoke())
    } catch (e: IOException) {
        Timber.d("Network error ${e.message}")
        Error(ErrorType.NetworkError)
    } catch (e: HttpException) {
        Timber.d("Server error ${e.message}")
        Error(ErrorType.ServerError, e.message)
    } catch (e: Exception) {
        Timber.d("Unknown error ${e.message}")
        Error(ErrorType.ServerError, e.message)
    }
}
