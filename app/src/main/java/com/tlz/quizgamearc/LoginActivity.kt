package com.tlz.quizgamearc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tlz.quizgamearc.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var loginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root
        setContentView(R.layout.activity_login)

        loginBinding.btnSignIn.setOnClickListener {

        }

        loginBinding.btnGoogleSignIn.setOnClickListener {

        }

        loginBinding.tvSignUp.setOnClickListener {

        }

        loginBinding.tvForgotPassword.setOnClickListener {

        }



    }
}