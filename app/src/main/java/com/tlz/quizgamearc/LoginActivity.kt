package com.tlz.quizgamearc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.tlz.quizgamearc.databinding.ActivityLoginBinding
import kotlin.math.log

class LoginActivity : AppCompatActivity() {

    lateinit var loginBinding: ActivityLoginBinding

    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root
        setContentView(view)

        loginBinding.btnSignIn.setOnClickListener {
            val userEmail = loginBinding.etLoginEmail.text.toString()
            val userPassword = loginBinding.etLoginPassword.text.toString()

            signInUser(userEmail, userPassword)
        }

        loginBinding.btnGoogleSignIn.setOnClickListener {

        }

        loginBinding.tvSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        loginBinding.tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }


    }

    fun signInUser(userEmail : String, userPassword : String) {
        auth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                Toast.makeText(applicationContext, "Welcome!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val user = auth.currentUser

        if (user != null) {
            Toast.makeText(applicationContext, "Welcome!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}