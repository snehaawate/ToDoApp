package com.dewan.todoapp.model.remote.response.auth

import com.google.gson.annotations.SerializedName

data class ValidateResponse(
    @SerializedName("message")
    val message: String
) {
}