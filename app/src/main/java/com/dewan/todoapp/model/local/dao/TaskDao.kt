package com.dewan.todoapp.model.local.dao

import androidx.room.*
import com.dewan.todoapp.model.local.entity.TaskEntity

@Dao
interface TaskDao {

    @Insert
    suspend fun insert(taskEntity: TaskEntity): Long

    @Insert
    suspend fun insertMany(taskEntity: List<TaskEntity>): List<Long>

    @Update
    suspend fun update(taskEntity: TaskEntity): Int

    @Delete
    suspend fun delete(taskEntity: TaskEntity): Int

    @Query("SELECT * FROM task_entity ORDER BY taskId desc")
    suspend fun getAllTaskFromDd(): List<TaskEntity>

    @Query("SELECT MAX(taskId) FROM task_entity")
    suspend fun getMaxTaskId(): Int
}