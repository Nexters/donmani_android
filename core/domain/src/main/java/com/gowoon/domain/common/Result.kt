package com.gowoon.domain.common

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val code: Int? = null, val message: String? = null) : Result<Nothing>
}