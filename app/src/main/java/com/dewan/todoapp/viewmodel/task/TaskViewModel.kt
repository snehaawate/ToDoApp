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
import com.dewan.todoapp.model.remote.Networking
import com.dewan.todoapp.model.remote.request.todo.AddTaskRequest
import com.dewan.todoapp.model.repository.AddTaskRepository
import retrofit2.HttpException


class TaskViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "TaskViewModel"
    }

    private val networkService = Networking.create(BuildConfig.BASE_URL)
    private var addTaskRepository: AddTaskRepository
    private var sharesPreferences = application.getSharedPreferences(BuildConfig.PREF_NAME,Context.MODE_PRIVATE)
    private var appPreferences: AppPreferences
    private var token: String = ""
    val userId: MutableLiveData<Int> = MutableLiveData()
    val progress: MutableLiveData<Boolean> = MutableLiveData()
    private val isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()

    init {
        addTaskRepository = AddTaskRepository(networkService)
        appPreferences = AppPreferences(sharesPreferences)
        token = appPreferences.getAccessToken().toString()
        userId.value = appPreferences.getUserId()
    }


    fun addTask(addTaskRequest: AddTaskRequest) = liveData {
        try {
            progress.value = true

            val data =  addTaskRepository.addTask(token,addTaskRequest)
            isSuccess.value = data.code() == 201
            emit(isSuccess.value)

            progress.value = false


        }
        catch (httpException: HttpException){
            Log.e(TAG,httpException.toString())
            isError.value = httpException.toString()

        }
        catch (exception: Exception){
            Log.e(TAG,exception.toString())
            isError.value = exception.toString()
        }


    }
}
