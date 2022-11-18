package com.dewan.todoapp.util

import java.util.regex.Pattern

object Validator {

    private const val MIN_PASSWORD_LENGTH = 7
    private val EMAIL_ADDRESS = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    fun validateEmail(email: String): Boolean {
        return  when {
            email.isBlank() ->
                false
            !EMAIL_ADDRESS.matcher(email).matches() ->
                false
            else ->
                true
        }

    }

    fun validatePassword(password: String): Boolean {
        return when {
            password.isBlank() ->
                false
            password.length < MIN_PASSWORD_LENGTH ->
                false
            else ->
                true
        }
    }
}