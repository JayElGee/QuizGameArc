package com.tlz.quizgamearc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.tlz.quizgamearc.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var forgotBinding : ActivityForgotPasswordBinding

    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgotBinding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = forgotBinding.root
        setContentView(view)

        forgotBinding.btnReset.setOnClickListener {

            val userEmail = forgotBinding.etForgotEmail.text.toString()

            auth.sendPasswordResetEmail(userEmail).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Your password has been reset. Please check your email for further instructions.", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}