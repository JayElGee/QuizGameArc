package com.tlz.quizgamearc

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.tlz.quizgamearc.databinding.ActivityLoginBinding
import kotlin.math.log

class LoginActivity : AppCompatActivity() {

    lateinit var loginBinding: ActivityLoginBinding

    val auth = FirebaseAuth.getInstance()

    lateinit var googleSignInClient: GoogleSignInClient

    lateinit var activityResultLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root
        setContentView(view)

        val googleBtnText = loginBinding.btnGoogleSignIn.getChildAt(0) as TextView
        googleBtnText.text = "Sign in with Google"
        googleBtnText.setTextColor(Color.BLACK)
        googleBtnText.textSize = 18F

        registerActivityForGoogleSignIn()

        loginBinding.btnSignIn.setOnClickListener {
            val userEmail = loginBinding.etLoginEmail.text.toString()
            val userPassword = loginBinding.etLoginPassword.text.toString()

            signInUser(userEmail, userPassword)
        }

        loginBinding.btnGoogleSignIn.setOnClickListener {
            signInGoogle()
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

    private fun signInGoogle() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("224424342605-r3fjuskcklrkh56n7lannol5065g16sb.apps.googleusercontent.com")
            .requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        signIn()

    }

    private fun signIn() {

        val signInIntent : Intent = googleSignInClient.signInIntent
        activityResultLauncher.launch(signInIntent)
    }

    private fun registerActivityForGoogleSignIn() {

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {result ->

                val resultCode = result.resultCode
                val data = result.data

                if (resultCode == RESULT_OK && data != null) {

                    val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
                    firebaseSignInWithGoogle(task)
                }
        })

    }

    private fun firebaseSignInWithGoogle(task: Task<GoogleSignInAccount>) {

        try {
            val acc : GoogleSignInAccount = task.getResult(ApiException::class.java)
            Toast.makeText(applicationContext, "Welcome to Quiz Arc - the quiz game!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            firebaseGoogleAccount(acc)
        } catch (e : ApiException) {
            Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseGoogleAccount(account: GoogleSignInAccount) {

        val authCredential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(authCredential).addOnCompleteListener { task ->

            if (task.isSuccessful) {

            } else {

            }
        }
    }

}