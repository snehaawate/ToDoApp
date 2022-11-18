package com.dewan.todoapp.viewmodel.task

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dewan.todoapp.BuildConfig
import com.dewan.todoapp.model.local.AppPreferences
import com.dewan.todoapp.model.local.db.AppDatabase
import com.dewan.todoapp.model.local.entity.TaskEntity
import com.dewan.todoapp.model.remote.Networking
import com.dewan.todoapp.model.remote.request.todo.DeleteTaskRequest
import com.dewan.todoapp.model.repository.DeleteTaskRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class TaskDetailViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "TaskDetailViewModel"
    }


    private val networkService = Networking.create(BuildConfig.BASE_URL)
    private lateinit var deleteTaskRepository: DeleteTaskRepository
    private var sharesPreferences = application.getSharedPreferences(BuildConfig.PREF_NAME, Context.MODE_PRIVATE)
    private var appPreferences: AppPreferences
    private var userId: String
    private var token: String = ""

    val idField: MutableLiveData<String> = MutableLiveData()
    val taskId: MutableLiveData<String> = MutableLiveData()
    val dataTime: MutableLiveData<String> = MutableLiveData()
    val title: MutableLiveData<String> = MutableLiveData()
    val body : MutableLiveData<String> = MutableLiveData()
    val note : MutableLiveData<String> = MutableLiveData()
    val status : MutableLiveData<String> = MutableLiveData()
    val userIdField : MutableLiveData<String> = MutableLiveData()
    val bgColor : MutableLiveData<String> = MutableLiveData()
    val isValidUser: MutableLiveData<Boolean> = MutableLiveData()
    val isDeleted: MutableLiveData<Boolean> = MutableLiveData()


    init {
        deleteTaskRepository = DeleteTaskRepository(networkService, AppDatabase.getInstance(application))
        appPreferences = AppPreferences(sharesPreferences)
        userId = appPreferences.getUserId().toString()
        token = appPreferences.getAccessToken().toString()
    }



    fun checkUserId(){
        try {
            isValidUser.value = userIdField.value == userId
        }
        catch (httpException: HttpException){
            Log.e(TAG,httpException.toString())

        }
        catch (exception: Exception){
            Log.e(TAG,exception.toString())
        }

    }

    fun deleteTask(){
        viewModelScope.launch {
           val response = deleteTaskRepository.deleteTaskFromApi(token, DeleteTaskRequest(taskId.value!!,userId))

            if (response.code() == 200){

                response.body()?.run {
                     val result = deleteTaskRepository.deleteTaskFromDb(TaskEntity(
                         id = idField.value!!.toLong(),
                        taskId = this.id,
                        title = this.title,
                        body = this.body,
                        status = this.status,
                        userId = this.userId,
                        createdAt = this.createdAt,
                        updatedAt = this.updatedAt

                    ))

                    if (result >= 1){
                        isDeleted.postValue(true)
                        Log.d(TAG, "Delete row: $result")
                    }
                    else {
                        isDeleted.postValue(false)
                        Log.d(TAG, "Delete row was a error.")
                    }


                }

            }
        }
    }
}
