package com.lawgicalai.bubbychat.data.di.utils

import kotlinx.io.IOException
import java.net.SocketTimeoutException

sealed class ApiResult<out T> {
    data class Success<out T>(
        val data: T,
    ) : ApiResult<T>()

    data class Error(
        val exception: Exception,
    ) : ApiResult<Nothing>()
}

suspend fun <T> safeApiCall(apiCall: suspend () -> T): ApiResult<T> =
    try {
        ApiResult.Success(apiCall())
    } catch (e: SocketTimeoutException) {
        println("Connection timed out.")
        ApiResult.Error(e)
    } catch (e: IOException) {
        println("Network error.")
        ApiResult.Error(e)
    } catch (e: Exception) {
        println("An unexpected error occurred: ${e.message}")
        ApiResult.Error(e)
    }
