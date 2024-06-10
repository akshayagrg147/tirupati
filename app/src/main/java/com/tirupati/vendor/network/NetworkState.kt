package com.tirupati.vendor.network

import okhttp3.ResponseBody


sealed class NetworkState<out T : Any> {
    data class Success<T : Any>(val body: T) : NetworkState<T>()
    object InvalidData : NetworkState<Nothing>()
    data class Error<E : Any>(val msg: E) : NetworkState<Nothing>()
    data class NetworkException(val msg: String) : NetworkState<Nothing>()
    sealed class HttpErrors : NetworkState<Nothing>() {
        data class BadRequest(val msg: String):HttpErrors()
        data class Unauthorized(val msg:String):HttpErrors()
        data class ServiceUnavailable(val msg: String):HttpErrors()
        data class WrongData(val msg: ResponseBody?):HttpErrors()
//        ==============================================================
        data class ResourceForbidden(val msg: String) : HttpErrors()
        data class ResourceNotFound(val msg: String) : HttpErrors()
        data class InternalServerError(val msg: String) : HttpErrors()
        data class BadGateWay(val msg: String) : HttpErrors()
        data class ResourceRemoved(val msg: String) : HttpErrors()
        data class RemovedResourceFound(val msg: String) : HttpErrors()

    }

    // Here msg is exception and error
}
