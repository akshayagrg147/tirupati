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
                when (response.code()) {
                    400 -> NetworkState.HttpErrors.BadRequest(response.message())
                    401 -> NetworkState.HttpErrors.Unauthorized(response.message())
                    503 -> NetworkState.HttpErrors.ServiceUnavailable(response.message())

                    422 -> NetworkState.HttpErrors.WrongData(response.errorBody())
                    403 -> NetworkState.HttpErrors.ResourceForbidden(response.message())
                    404 -> NetworkState.HttpErrors.ResourceNotFound(response.message())
                    500 -> NetworkState.HttpErrors.InternalServerError(response.message())
                    502 -> NetworkState.HttpErrors.BadGateWay(response.message())
                    301 -> NetworkState.HttpErrors.ResourceRemoved(response.message())
                    302 -> NetworkState.HttpErrors.RemovedResourceFound(response.message())
                    else -> NetworkState.Error(response.message())
                }
            }

        } catch (error: JsonSyntaxException) {
            NetworkState.NetworkException("Server Model Issue")
        }catch (error: IOException) {
            NetworkState.NetworkException("Server Model Issue"+error.message)
        }
    }
}

