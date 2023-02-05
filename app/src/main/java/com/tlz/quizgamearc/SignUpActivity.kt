package com.tlz.quizgamearc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.tlz.quizgamearc.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    lateinit var signUpBinding: ActivitySignUpBinding

    val auth : FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = signUpBinding.root
        setContentView(view)

        signUpBinding.btnSignUp.setOnClickListener {
            val email = signUpBinding.etSignUpEmail.text.toString()
            val password = signUpBinding.etSignUpPassword.text.toString()

            signUpWithFirebase(email, password)
        }
    }

    fun signUpWithFirebase(email : String, password : String) {
        signUpBinding.pbSignUp.visibility = View.VISIBLE
        signUpBinding.btnSignUp.isClickable = false

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            if (task.isSuccessful) {

                Toast.makeText(applicationContext, "Your account has been created.", Toast.LENGTH_SHORT).show()
                finish()
                signUpBinding.pbSignUp.visibility = View.INVISIBLE
                signUpBinding.btnSignUp.isClickable = true
            } else {
                Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}