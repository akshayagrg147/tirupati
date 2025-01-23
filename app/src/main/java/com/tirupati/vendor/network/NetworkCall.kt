package com.tirupati.vendor.network
import android.util.Log
import com.google.gson.JsonSyntaxException
import retrofit2.Response
import java.io.IOException

abstract class NetworkCall {
    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): NetworkState<T> {
        return try {
            val response = call.invoke()
                Log.d("kjkjkj",response.toString())

            if (response.isSuccessful) {
                NetworkState.Success(response.body()!!)

            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"

                Log.d("errormessage",errorMessage)
                when (response.code()) {
                    400 -> NetworkState.HttpErrors.BadRequest(errorMessage)
                    401 -> NetworkState.HttpErrors.Unauthorized(errorMessage)
                    503 -> NetworkState.HttpErrors.ServiceUnavailable(response.message())

                    422 -> NetworkState.HttpErrors.WrongData(response.errorBody())
                    403 -> NetworkState.HttpErrors.ResourceForbidden(errorMessage)
                    404 -> NetworkState.HttpErrors.ResourceNotFound(errorMessage)
                    500 -> NetworkState.HttpErrors.InternalServerError(errorMessage)
                    502 -> NetworkState.HttpErrors.BadGateWay(errorMessage)
                    301 -> NetworkState.HttpErrors.ResourceRemoved(errorMessage)
                    302 -> NetworkState.HttpErrors.RemovedResourceFound(errorMessage)
                    else -> NetworkState.Error(errorMessage)
                }
            }

        } catch (error: Exception) {
            NetworkState.NetworkException(error.message?:"something went wrong")
        }
    }
}

