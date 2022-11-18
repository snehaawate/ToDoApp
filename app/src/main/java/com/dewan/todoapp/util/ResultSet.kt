package com.dewan.todoapp.util

/*
created by Richard Dewan 27/09/2020
*/

sealed class ResultSet<out T: Any> {

    data class Success<out T: Any>(val data: Any?) : ResultSet<T>()

    data class Error(val error: Exception? = null, val errorMsg: Int = 0, val code: Int = 0): ResultSet<Nothing>()
}