package com.dewan.todoapp.viewmodel.task

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dewan.todoapp.BuildConfig
import com.dewan.todoapp.model.local.AppPreferences
import com.dewan.todoapp.model.local.db.AppDatabase
import com.dewan.todoapp.model.local.entity.TaskEntity
import com.dewan.todoapp.model.remote.Networking
import com.dewan.todoapp.model.remote.request.todo.EditTaskRequest
import com.dewan.todoapp.model.remote.response.todo.EditTaskResponse
import com.dewan.todoapp.model.repository.AddTaskRepository
import com.dewan.todoapp.model.repository.EditTaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class EditTaskViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "EditTaskViewModel"
    }

    private val networkService = Networking.create(BuildConfig.BASE_URL)
    private var editTaskRepository: EditTaskRepository
    private var sharesPreferences =
        application.getSharedPreferences(BuildConfig.PREF_NAME, Context.MODE_PRIVATE)
    private var appPreferences: AppPreferences
    private var token: String = ""
    val userId: MutableLiveData<Int> = MutableLiveData()

    val id: MutableLiveData<String> = MutableLiveData()
    val taskId: MutableLiveData<String> = MutableLiveData()
    val title: MutableLiveData<String> = MutableLiveData()
    val body: MutableLiveData<String> = MutableLiveData()
    val note: MutableLiveData<String> = MutableLiveData()
    val status: MutableLiveData<String> = MutableLiveData()
    val index: MutableLiveData<Int> = MutableLiveData()
    val taskList: ArrayList<String> = ArrayList()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()

    init {

        editTaskRepository =
            EditTaskRepository(networkService, AppDatabase.getInstance(application))

        appPreferences = AppPreferences(sharesPreferences)
        token = appPreferences.getAccessToken().toString()
        userId.value = appPreferences.getUserId()
    }


    fun getIndexFromTaskList() {
        index.value = taskList.indexOf(status.value)
    }

    /*
    this is to update the task in api
     */
    fun editTask() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                loading.postValue(true)
                val data = editTaskRepository.editTak(
                    token, EditTaskRequest(
                        taskId.value!!.toInt(),
                        userId.value.toString(),
                        title.value.toString(),
                        body.value.toString(),
                        note.value!!.toString(),
                        status.value.toString()
                    )
                )
                if (data.code() == 201) {
                    updateTask(data.body()!!)
                    isSuccess.postValue(true)
                } else {
                    isSuccess.postValue(false)
                }

                loading.postValue(false)

            } catch (httpException: HttpException) {
                Log.e(TAG, httpException.toString())
                isError.postValue(httpException.toString())
                loading.postValue(false)

            } catch (exception: Exception) {
                Log.e(TAG, exception.toString())
                isError.postValue(exception.toString())
                loading.postValue(false)
            }

        }

    }

    /*
    this is to update the task in local db
     */
    private fun updateTask(editTaskResponse: EditTaskResponse) {

        try {

            CoroutineScope(Dispatchers.IO).launch {
                loading.postValue(true)

                val id = editTaskRepository.updateTask(
                    TaskEntity(
                        id = id.value!!.toLong(),
                        taskId = editTaskResponse.id,
                        title = editTaskResponse.title,
                        body = editTaskResponse.body,
                        note = editTaskResponse.note,
                        status = editTaskResponse.status,
                        userId = editTaskResponse.userId.toInt(),
                        createdAt = editTaskResponse.createdAt,
                        updatedAt = editTaskResponse.updatedAt
                    )
                )

                if (id > 0) {
                    Log.e(TAG, "Update Success: $id")
                }

                loading.postValue(false)
            }

        } catch (error: Exception) {
            Log.e(TAG, error.toString())
        }
    }


}
