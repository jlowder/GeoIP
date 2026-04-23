package com.example.ipgeolookup.data.model

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

// Extension function for easy success creation
fun <T> Result<T>.success(): Result<T> = Result.Success(this as T)