package com.dewan.todoapp.model.repository

import com.dewan.todoapp.model.local.db.AppDatabase
import com.dewan.todoapp.model.local.entity.TaskEntity
import com.dewan.todoapp.model.remote.NetworkService
import com.dewan.todoapp.model.remote.request.todo.EditTaskRequest
import com.dewan.todoapp.model.remote.response.todo.EditTaskResponse
import retrofit2.Response

class EditTaskRepository(
    private val networkService: NetworkService,
    private val appDatabase: AppDatabase
) {

    suspend fun editTak(
        token: String,
        editTaskRequest: EditTaskRequest
    ): Response<EditTaskResponse> =
        networkService.editTask(token, editTaskRequest)

    suspend fun updateTask(taskEntity: TaskEntity)=  appDatabase.taskDao().update(taskEntity)
}