package com.dewan.todoapp.model.remote.request.todo

import com.google.gson.annotations.SerializedName

data class DeleteTaskRequest(
    @SerializedName("id")
    val id: String,
    @SerializedName("user_id")
    val user_id: String
){
}