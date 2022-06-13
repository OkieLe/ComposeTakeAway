package com.example.takeaway.domain.base

import retrofit2.HttpException
import java.io.IOException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val type: Category, val message: String? = "") : Result<T>()
}

sealed interface Category {
    object Unknown : Category
    object Network : Category
    object Server : Category
}

@OptIn(ExperimentalContracts::class)
fun <T> Result<T>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is Result.Success)
    }
    return this is Result.Success
}

fun <T> Result<T>.onSuccess(block: (T) -> Unit): Result<T> {
    if (isSuccess()) {
        block(data)
    }
    return this
}

fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Result.Success<T>)?.data ?: fallback
}

@OptIn(ExperimentalContracts::class)
fun <T> Result<T>.isError(): Boolean {
    contract {
        returns(true) implies (this@isError is Result.Error)
    }
    return this is Result.Error
}

fun <T> Result<T>.onError(block: (Category, String?) -> Unit): Result<T> {
    if (isError()) {
        block(type, message)
    }
    return this
}

fun <T> wrapError(throwable: Throwable): Result.Error<T> {
    return when (throwable) {
        is IOException -> Result.Error(Category.Network)
        is HttpException -> Result.Error(Category.Server)
        else -> Result.Error(Category.Unknown, throwable.message)
    }
}
