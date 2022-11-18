package com.dewan.todoapp.util.network

import com.google.gson.annotations.SerializedName

data class NetworkError(
    @SerializedName("statusCode")
    val statusCode: Int = -1,
    @SerializedName("message")
    val message: String = "Something went wrong. Please try again."
) {
}