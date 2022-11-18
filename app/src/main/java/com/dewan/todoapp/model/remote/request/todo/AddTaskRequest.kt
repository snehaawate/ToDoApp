package com.dewan.todoapp.model.remote.request.todo


import com.google.gson.annotations.SerializedName

data class AddTaskRequest(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("body")
    val body: String,
    @SerializedName("note")
    val note: String,
    @SerializedName("status")
    val status: String
)