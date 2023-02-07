package com.tlz.quizgamearc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.tlz.quizgamearc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        mainBinding.btnBeginQuiz.setOnClickListener {

            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }

        mainBinding.btnLogOut.setOnClickListener {

//          Only logs out regular sign in with email and password
            FirebaseAuth.getInstance().signOut()

//          Signs out Google account
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build()
            val googleSignInClient = GoogleSignIn.getClient(this, gso)
            googleSignInClient.signOut().addOnCompleteListener {task ->

                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Signed Out!", Toast.LENGTH_SHORT).show()
                }
            }

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}