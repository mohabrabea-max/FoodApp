package com.example.applicationhome.core.data.remote.util

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException
import kotlin.time.Duration.Companion.milliseconds

suspend fun <T> retryLocally(
    times : Int = 3,
    initialDelay : Long = 1500,
    block : suspend  () -> T
): Result<T> {
    var currentDelay = initialDelay

    repeat(times){ attempt ->
        try {
            return Result.success(block())
        } catch (e: Exception) {
            if(e is CancellationException) throw e

            val isRetryable = isRetryableException(e)

            if (!isRetryable || attempt == times - 1) {
                return Result.failure(e)
            }

            delay(currentDelay.milliseconds)
            currentDelay *= 2
        }
    }
    return Result.failure(Exception("Unknown error"))
}

fun isRetryableException(e: Exception): Boolean {
    return when (e) {
        is IOException -> true

        is HttpException -> {
            val code = e.code()
            code in 500..599 || code == 429
        }

        else -> false
    }
}