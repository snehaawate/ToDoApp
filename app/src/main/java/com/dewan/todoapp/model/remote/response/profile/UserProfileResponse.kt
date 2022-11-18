package com.dewan.todoapp.model.remote.response.profile


import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("profile_image")
    val profileImage: String,
    @SerializedName("profile_image_path")
    val profileImagePath: String,
    @SerializedName("bio")
    val bio: String
)