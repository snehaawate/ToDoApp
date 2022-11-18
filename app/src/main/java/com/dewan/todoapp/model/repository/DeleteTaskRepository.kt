package com.dewan.todoapp.model.repository

import com.dewan.todoapp.model.local.db.AppDatabase
import com.dewan.todoapp.model.local.entity.TaskEntity
import com.dewan.todoapp.model.remote.NetworkService
import com.dewan.todoapp.model.remote.request.todo.DeleteTaskRequest

class DeleteTaskRepository(
    private val networkService: NetworkService,
    private val appDatabase: AppDatabase
) {

    suspend fun deleteTaskFromApi(token: String, deleteTaskRequest: DeleteTaskRequest) =
        networkService.deleteTask(token, deleteTaskRequest)

    suspend fun deleteTaskFromDb(taskEntity: TaskEntity) =
        appDatabase.taskDao().delete(taskEntity)
}