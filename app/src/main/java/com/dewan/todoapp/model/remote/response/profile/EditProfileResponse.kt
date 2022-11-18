package com.dewan.todoapp.model.remote.response.profile


import com.google.gson.annotations.SerializedName

data class EditProfileResponse(
    @SerializedName("success")
    val success: Boolean
)