package com.dewan.todoapp.model.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Networking {

    fun create(baseUrl: String): NetworkService{
        return  Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NetworkService::class.java)
    }
}