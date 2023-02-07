package com.tlz.quizgamearc

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tlz.quizgamearc.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {

    lateinit var quizBinding : ActivityQuizBinding

    lateinit var timer : CountDownTimer
    private val totalTime = 25000L
    var timerContinue = false
    var timeLeft = totalTime

    val db = FirebaseDatabase.getInstance()
    val dbReference = db.reference.child("questions")

    var question = ""
    var answerA = ""
    var answerB = ""
    var answerC = ""
    var answerD = ""
    var correctAnswer = ""
    var questionCount = 0
    var questionNumber = 1

    var userAnswer = ""
    var userCorrect = 0
    var userWrong = 0

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val scoreRef = db.reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizBinding = ActivityQuizBinding.inflate(layoutInflater)
        val view = quizBinding.root
        setContentView(view)

        gameLogic()

        quizBinding.btnNext.setOnClickListener {

            resetTimer()
            gameLogic()
        }

        quizBinding.btnEnd.setOnClickListener {
            sendScore()
        }

        quizBinding.tvA.setOnClickListener {

            pauseTimer()
            userAnswer = "a"

            if (correctAnswer == userAnswer) {
                quizBinding.tvA.setBackgroundColor(Color.GREEN)
                userCorrect++
                quizBinding.tvCorrect.text = userCorrect.toString()
            } else {
                quizBinding.tvA.setBackgroundColor(Color.RED)
                userWrong++
                quizBinding.tvWrong.text = userWrong.toString()
                findAnswer()
            }
            disableClickableOptions()
        }

        quizBinding.tvB.setOnClickListener {

            pauseTimer()
            userAnswer = "b"

            if (correctAnswer == userAnswer) {
                quizBinding.tvB.setBackgroundColor(Color.GREEN)
                userCorrect++
                quizBinding.tvCorrect.text = userCorrect.toString()
            } else {
                quizBinding.tvB.setBackgroundColor(Color.RED)
                userWrong++
                quizBinding.tvWrong.text = userWrong.toString()
                findAnswer()
            }
            disableClickableOptions()
        }

        quizBinding.tvC.setOnClickListener {

            pauseTimer()
            userAnswer = "c"

            if (correctAnswer == userAnswer) {
                quizBinding.tvC.setBackgroundColor(Color.GREEN)
                userCorrect++
                quizBinding.tvCorrect.text = userCorrect.toString()
            } else {
                quizBinding.tvC.setBackgroundColor(Color.RED)
                userWrong++
                quizBinding.tvWrong.text = userWrong.toString()
                findAnswer()
            }
            disableClickableOptions()
        }

        quizBinding.tvD.setOnClickListener {

            pauseTimer()
            userAnswer = "d"

            if (correctAnswer == userAnswer) {
                quizBinding.tvD.setBackgroundColor(Color.GREEN)
                userCorrect++
                quizBinding.tvCorrect.text = userCorrect.toString()
            } else {
                quizBinding.tvD.setBackgroundColor(Color.RED)
                userWrong++
                quizBinding.tvWrong.text = userWrong.toString()
                findAnswer()
            }
            disableClickableOptions()
        }
    }

    private fun gameLogic() {

        restoreOptions()

        dbReference.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                questionCount = snapshot.childrenCount.toInt()

                if (questionNumber <= questionCount) {
                    question = snapshot.child(questionNumber.toString()).child("q").value.toString()
                    answerA = snapshot.child(questionNumber.toString()).child("a").value.toString()
                    answerB = snapshot.child(questionNumber.toString()).child("b").value.toString()
                    answerC = snapshot.child(questionNumber.toString()).child("c").value.toString()
                    answerD = snapshot.child(questionNumber.toString()).child("d").value.toString()
                    correctAnswer = snapshot.child(questionNumber.toString()).child("answer").value.toString()

                    quizBinding.tvQuestion.text = question
                    quizBinding.tvA.text = answerA
                    quizBinding.tvB.text = answerB
                    quizBinding.tvC.text = answerC
                    quizBinding.tvD.text = answerD

                    quizBinding.pbQuiz.visibility = View.INVISIBLE
                    quizBinding.linLayoutInfo.visibility = View.VISIBLE
                    quizBinding.linLayoutQuestion.visibility = View.VISIBLE
                    quizBinding.linLayoutButtons.visibility = View.VISIBLE

                    startTimer()

                } else {

                    val dialogMessage = AlertDialog.Builder(this@QuizActivity)
                    dialogMessage.setTitle("Quiz Arc")
                    dialogMessage.setMessage("Congratulations! \nYou have completed all the questions. Would you like to see the results?")
                    dialogMessage.setCancelable(false)
                    dialogMessage.setPositiveButton("See Results") { dialogWindow, position ->
                        sendScore()
                    }
                    dialogMessage.setNegativeButton("Play Again") { dialogWindow, position ->
                        val intent = Intent(this@QuizActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    dialogMessage.create().show()
                }

                questionNumber++

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun findAnswer() {

        when(correctAnswer) {
            "a" -> quizBinding.tvA.setBackgroundColor(Color.GREEN)
            "b" -> quizBinding.tvB.setBackgroundColor(Color.GREEN)
            "c" -> quizBinding.tvC.setBackgroundColor(Color.GREEN)
            "d" -> quizBinding.tvD.setBackgroundColor(Color.GREEN)

        }
    }

    fun disableClickableOptions() {

        quizBinding.tvA.isClickable = false
        quizBinding.tvB.isClickable = false
        quizBinding.tvC.isClickable = false
        quizBinding.tvD.isClickable = false
    }

    fun restoreOptions() {

        quizBinding.tvA.setBackgroundColor(Color.WHITE)
        quizBinding.tvB.setBackgroundColor(Color.WHITE)
        quizBinding.tvC.setBackgroundColor(Color.WHITE)
        quizBinding.tvD.setBackgroundColor(Color.WHITE)

        quizBinding.tvA.isClickable = true
        quizBinding.tvB.isClickable = true
        quizBinding.tvC.isClickable = true
        quizBinding.tvD.isClickable = true
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeft, 1000) {
            override fun onTick(millisecondsUntilFinish: Long) {

                timeLeft = millisecondsUntilFinish
                updateCountDownText()
            }

            override fun onFinish() {

                disableClickableOptions()
                resetTimer()
                updateCountDownText()
                quizBinding.tvQuestion.text = "Time has expired. On to the next question."
                timerContinue = false
            }

        }.start()

        timerContinue = true
    }

    fun updateCountDownText() {

        val remainingTime : Int = (timeLeft / 1000).toInt()
        quizBinding.tvTime.text = remainingTime.toString()
    }

    fun pauseTimer() {

        timer.cancel()
        timerContinue = false
    }

    fun resetTimer() {

        pauseTimer()
        timeLeft = totalTime
        updateCountDownText()
    }

    fun sendScore() {

        user?.let {
            val userUID = it.uid
            scoreRef.child("scores").child(userUID).child("correct").setValue(userCorrect)
            scoreRef.child("scores").child(userUID).child("wrong").setValue(userWrong).addOnSuccessListener {

                Toast.makeText(applicationContext,"Scores successfully updated.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@QuizActivity, ResultActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}