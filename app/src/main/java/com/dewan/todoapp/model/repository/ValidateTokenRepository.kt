package com.dewan.todoapp.model.repository

import com.dewan.todoapp.model.remote.NetworkService

class ValidateTokenRepository(private val networkService: NetworkService) {

    suspend fun validateToken(token:String) = networkService.validateToken(token)
}