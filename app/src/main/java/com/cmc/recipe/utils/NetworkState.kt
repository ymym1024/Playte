package com.cmc.recipe.utils

sealed class NetworkState<out T>{
    object Loading :NetworkState<Nothing>()

    data class Success<out T>(val data: T) : NetworkState<T>()

    data class Error(val code: Int, val message: Any?) : NetworkState<Nothing>()

    data class Exception(val e: Throwable) : NetworkState<Nothing>()
}
