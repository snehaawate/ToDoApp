package com.dewan.todoapp.view.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dewan.todoapp.R
import com.dewan.todoapp.model.remote.request.auth.LoginRequest
import com.dewan.todoapp.model.remote.response.auth.LoginResponse
import com.dewan.todoapp.util.Validator
import com.dewan.todoapp.view.ui.main.MainActivity
import com.dewan.todoapp.viewmodel.auth.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.alert


class LoginActivity : AppCompatActivity() {

    companion object {
        const val TAG = "LoginActivity"
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        //login
        btn_login.onClick {prepareLogin() }

        // lunch the signup activity
        tv_signup.onClick { lunchSignUpActivity() }

        //observer
        observer()


    }

    private fun lunchSignUpActivity(){

        startActivity(intentFor<SignUpActivity>())
    }

    private fun prepareLogin(){
        val email = txt_userId.text.toString()
        val password = txt_password.text.toString()

        if (!Validator.validateEmail(email)){
            alert {
                isCancelable = false
                title = getString(R.string.validator_title)
                message = getString(R.string.validation_email_failed)
                positiveButton("OK"){
                    it.dismiss()
                }
            }.show()
        }
        else if (!Validator.validatePassword(password)){
            alert {
                isCancelable = false
                title = getString(R.string.validator_title)
                message = getString(R.string.validation_password_failed)
                positiveButton("OK"){
                    it.dismiss()
                }
            }.show()
        }
        else {
            val loginRequest = LoginRequest(email,password)

            login(loginRequest)
        }

        //check for the empty value
        /*when {
            email.isEmpty() -> {

                alert {
                    isCancelable = false
                    title = getString(R.string.empty_email_title)
                    message = getString(R.string.empty_email_msg)
                    positiveButton("OK"){
                        it.dismiss()
                    }
                }.show()

            }
            password.isEmpty() -> {

                alert {
                    isCancelable = false
                    title = getString(R.string.empty_password_title)
                    message = getString(R.string.empty_password_msg)
                    positiveButton("OK"){
                        it.dismiss()
                    }
                }.show()
            }
            else -> {
                val loginRequest = LoginRequest(email,password)

                login(loginRequest)
            }
        }
         */

    }

    private fun login(login_request: LoginRequest){
        viewModel.doLogin(login_request)
    }


    private fun observer(){
        viewModel.isError.observe(this, Observer {
            errorDialog(it)
        })

        viewModel.isSuccess.observe(this, Observer {
            if (it){
                finish()
                startActivity(intentFor<MainActivity>())

            }
            else {
                unSuccessFulDialog()
            }

        })

        viewModel.isLoading.observe(this, Observer {
            login_progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })
    }


    private fun unSuccessFulDialog(){
        alert {
            title = getString(R.string.title_un_successful_dialog)
            message = viewModel.errorMsg.value.toString()
            isCancelable = false
            positiveButton(getString(R.string.btn_ok)){dialog->
                dialog.dismiss()
            }
        }.show()
    }

    private fun errorDialog(errorMsg: String){
        alert {
            title = getString(R.string.title_error_dialog)
            message = errorMsg
            isCancelable = false
            positiveButton(getString(R.string.btn_ok)){dialog->
                dialog.dismiss()
            }
        }.show()

    }
}
