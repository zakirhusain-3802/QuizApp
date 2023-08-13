package com.yasma.quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.yasma.quiz.data.Result
import com.yasma.quiz.data.que_ans
import com.yasma.quiz.data.question
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//const val QBase_url="https://opentdb.com/"
//https://opentdb.com/api.php?amount=10&category=9&difficulty=medium&type=multiple
class question_dis : AppCompatActivity() {
    private lateinit var countDownTimer: CountDownTimer
    private val totalTime: Long = 20000 // 60 seconds in milliseconds
    private val interval: Long = 1// 1 second in milliseconds

    private   val datain = ArrayList<que_ans>()

    override fun onCreate(savedInstanceState: Bundle?) {
       // Countdown duration in milliseconds

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_dis)
        val cat=intent.getIntExtra("cat",0)
        val level=intent.getStringExtra("level")

        getqustions(cat,level.toString().lowercase())
        startCountdown()
    }


    private fun getqustions(cat: Int, level: String?) {
        val retrofitBuilder= Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(QBase_url)
            .build()
            .create(ApiInterface::class.java)
        val retfofitData=retrofitBuilder.getquestion(10,cat,level.toString().lowercase(),"multiple")
        retfofitData.enqueue(object : Callback<question?> {
            override fun onResponse(call: Call<question?>, response: Response<question?>) =
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val qeustion= responseBody?.results
                    println(responseBody!!.response_code.toString()+" que")

                    for(data in qeustion!!){
                        val q=data.question
                        val ans=data.correct_answer
                        val wrg=data.incorrect_answers
                        val queandans=que_ans(q.toString(),ans.toString(),wrg)
                        datain.add(queandans)


                    }
                    println(datain+" try")

                } else {
                    // Handle the error case, such as logging an error message
                    println("Error: ${response.code()} - ${response.message()}")
                }

            override fun onFailure(call: Call<question?>, t: Throwable) {
                println(t.message.toString())
                Toast.makeText(applicationContext,"Fails", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun startCountdown() {
        countDownTimer = object : CountDownTimer(totalTime, interval) {
            override fun onTick(millisUntilFinished: Long) {
                val progress = (millisUntilFinished.toFloat() / totalTime * 100).toInt()
                val progressBar=findViewById<ProgressBar>(R.id.progressBar)
                progressBar.progress = progress
                val secondsLeft = millisUntilFinished / 1000
                val timeLeftTextView=findViewById<TextView>(R.id.timeLeftTextView)
                timeLeftTextView.text = "Time Left: $secondsLeft s"

            }

            override fun onFinish() {
                // Countdown has finished, you can perform actions here
                val progressBar=findViewById<ProgressBar>(R.id.progressBar)
                progressBar.progress = 0
                val timeLeftTextView=findViewById<TextView>(R.id.timeLeftTextView)
                timeLeftTextView.text = "Time Left: 0 s"
            }
        }

        // Start the countdown timer when the activity starts
        countDownTimer.start()
    }
    override fun onDestroy() {
        super.onDestroy()
        // Cancel the countdown timer to prevent memory leaks
        countDownTimer.cancel()
    }

}






