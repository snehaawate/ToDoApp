package com.dewan.todoapp.viewmodel.auth

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.dewan.todoapp.BuildConfig
import com.dewan.todoapp.model.local.AppPreferences
import com.dewan.todoapp.model.remote.Networking
import com.dewan.todoapp.model.remote.request.auth.LoginRequest
import com.dewan.todoapp.model.remote.response.auth.LoginResponse
import com.dewan.todoapp.model.repository.LoginRepository
import com.dewan.todoapp.util.network.NetworkHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.HttpException


class LoginViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "LoginViewModel"
    }

    private val networkService = Networking.create(BuildConfig.BASE_URL)
    private var loginRepository: LoginRepository
    private var sharesPreferences =
        application.getSharedPreferences(BuildConfig.PREF_NAME, Context.MODE_PRIVATE)
    private var appPreferences: AppPreferences
    val loginResponse: MutableLiveData<LoginResponse> = MutableLiveData()
    val isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()
    val errorMsg: MutableLiveData<String> = MutableLiveData()


    init {
        appPreferences = AppPreferences(sharesPreferences)
        loginRepository = LoginRepository(networkService, appPreferences)
    }

    fun doLogin(loginRequest: LoginRequest) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoading.postValue(true)

                val data = loginRepository.login(loginRequest)
                if (data.code() == 200) {

                    saveUserPref(data.body()!!)
                    isSuccess.postValue(true)

                } else {
                    val error = NetworkHelper.handelNetworkError(data)
                    errorMsg.postValue(error.message)
                    isSuccess.postValue(false)
                }
                isLoading.postValue(false)


            } catch (httpException: HttpException) {
                Log.e(TAG, httpException.toString())
                isError.postValue(httpException.toString())
                isLoading.postValue(false)

            } catch (exception: Exception) {
                Log.e(TAG, exception.toString())
                isError.postValue(exception.toString())
                isLoading.postValue(false)
            }
        }

    }

    private fun saveUserPref(loginResponse: LoginResponse){

        viewModelScope.launch {
            loginRepository.saveUserDetail(loginResponse)
        }

    }

    /*

    fun login(loginRequest: LoginRequest) = liveData(IO) {
        try {
            isLoading.postValue(true)

            val data = loginRepository.login(loginRequest)
            if (data.code() == 200) {
                loginResponse.postValue(data.body())
                isSuccess.postValue(true)
                emit(loginResponse.value)
            } else {
                val error = NetworkHelper.handelNetworkError(data)
                errorMsg.postValue(error.message)
                isSuccess.postValue(false)
            }
            isLoading.postValue(false)


        } catch (httpException: HttpException) {
            Log.e(TAG, httpException.toString())
            isError.postValue(httpException.toString())
            isLoading.postValue(false)

        } catch (exception: Exception) {
            Log.e(TAG, exception.toString())
            isError.postValue(exception.toString())
            isLoading.postValue(false)
        }

    }

    fun saveUserDetail(loginResponse: LoginResponse) = liveData(IO) {
        val data = loginRepository.saveUserDetail(loginResponse)
        emit(data)
    }
    */

}