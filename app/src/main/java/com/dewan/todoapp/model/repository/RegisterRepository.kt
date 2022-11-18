package com.dewan.todoapp.model.repository

import com.dewan.todoapp.model.remote.NetworkService
import com.dewan.todoapp.model.remote.request.auth.RegisterRequest

class RegisterRepository(private val networkService: NetworkService) {

    suspend fun register(registerRequest: RegisterRequest) = networkService.register(registerRequest)
}