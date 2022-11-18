package com.dewan.todoapp.model.repository

import com.dewan.todoapp.model.remote.NetworkService
import com.dewan.todoapp.model.remote.request.todo.AddTaskRequest

class AddTaskRepository(private val networkService: NetworkService){

   suspend fun addTask(token: String,addTaskRequest: AddTaskRequest) = networkService.addTask(token,addTaskRequest)
}