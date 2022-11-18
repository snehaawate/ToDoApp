package com.dewan.todoapp.util

import com.dewan.todoapp.util.Validator.validateEmail
import com.dewan.todoapp.util.Validator.validatePassword
import org.junit.Assert.*
import org.junit.Test

class ValidatorTest {

    @Test
    fun validateEmail_whenValidEmail_returnTrue(){
        //GIVEN a valid email
        val email = "demo1@gmail.com"

        //WHEN you call a validate function validateEmail()
        val result  = validateEmail(email)

        //THEN we should get a true value
        assertEquals(true,result)
    }

    @Test
    fun validateEmail_whenInValidEmail_returnFalse(){
        //GIVEN invalid email
        val email = "demo1@gmailcom"

        //WHEN you call a validate function validateEmail()
        val result  = validateEmail(email)

        //THEN we should get a false value
        assertEquals(false,result)
    }



    @Test
    fun validatePassword_whenValidPassword_returnTrue(){
        //GIVEN valid password
        val password = "1234567"

        //WHEN you call a validate function validatePassword()
        val result = validatePassword(password)

        //THEN we should get true value
        assertEquals(true,result)


    }

    @Test
    fun validatePassword_whenInValidPassword_returnFalse(){
        //GIVEN in valid password
        val password = "123457"

        //WHEN you call a validate function validatePassword()
        val result = validatePassword(password)

        //THEN we should get false value
        assertEquals(false,result)

    }
}