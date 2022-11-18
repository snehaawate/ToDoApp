package com.dewan.todoapp.model.repository

import com.dewan.todoapp.model.remote.NetworkService
import com.dewan.todoapp.model.remote.response.profile.EditProfileResponse
import com.dewan.todoapp.model.remote.response.profile.UserProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class UserProfileRepository(private val networkService: NetworkService) {

    suspend fun getUserProfile(token: String, id: String): Response<UserProfileResponse> =
        networkService.getUserProfile(token, id)

    suspend fun editProfile(
        token: String,
        id: RequestBody,
        name: RequestBody,
        email: RequestBody,
        profile_image: MultipartBody.Part? = null,
        bio: RequestBody
    ): Response<EditProfileResponse> = networkService.editProfile(
        token,
        id,
        name,
        email,
        profile_image,
        bio
    )
}