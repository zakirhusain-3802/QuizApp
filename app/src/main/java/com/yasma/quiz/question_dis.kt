package com.yasma.quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.widget.*
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.yasma.quiz.data.Result
import com.yasma.quiz.data.que_ans
import com.yasma.quiz.data.question
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

//const val QBase_url="https://opentdb.com/"
//https://opentdb.com/api.php?amount=10&category=9&difficulty=medium&type=multiple
class question_dis : AppCompatActivity() {
    private  var s:Int=0
    private lateinit var countDownTimer: CountDownTimer
    private val totalTime: Long = 20000 // 60 seconds in milliseconds
    private val interval: Long = 1// 1 second in milliseconds

    private   val datain = ArrayList<que_ans>()

    private val handler = Handler()
    private val initialDelay: Long = 5000
    private val timeInterval: Long = 21000 // Time interval in milliseconds (e.g., 10 seconds)


    private val initialRunnable: Runnable = object : Runnable {
        override fun run() {
            // Call your initial function here
            val cat=intent.getIntExtra("cat",0)
            val level=intent.getStringExtra("level")

            getqustions(cat,level.toString().lowercase())

            // After initial delay, start the repeated execution
            handler.postDelayed(runnable, 5000)
        }
    }


    private val runnable: Runnable = object : Runnable {
        override fun run() {
            // Call your function here
            if(s!=10){
            myFunction()

            // Re-post the runnable after the specified time interval
            handler.postDelayed(this, timeInterval)
        }}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
       // Countdown duration in milliseconds

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_dis)
//        val cat=intent.getIntExtra("cat",0)
//        val level=intent.getStringExtra("level")

//        getqustions(cat,level.toString().lowercase())
        handler.postDelayed(initialRunnable, initialDelay)

//        startCountdown()
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
        private fun myFunction() {
              startCountdown()
            // Your function's code here
            val qno=findViewById<TextView>(R.id.qno)
            val question:String=datain[s].question
            val corret:String=datain[s].correct_answer
            val incorect=datain[s].incorrect_answers
            val option=ArrayList<String>()
            for(data in incorect){
                val o=data.toString()
                option.add(o.toString())
            }
            option.add(corret.toString())
            Collections.shuffle(option)
            println(option)
            val qutxt=findViewById<TextView>(R.id.questionTextView)
            val A=findViewById<RadioButton>(R.id.optionARadioButton)
            val B=findViewById<RadioButton>(R.id.optionBRadioButton)
            val C=findViewById<RadioButton>(R.id.optionCRadioButton)
            val D=findViewById<RadioButton>(R.id.optionDRadioButton)
            s=s+1
            qno.text="Q $s/10"
            qutxt.text=question
            A.text=option[0]
            B.text=option[1]
            C.text=option[2]
            D.text=option[3]

            val radioGroup = findViewById<RadioGroup>(R.id.optionsRadioGroup)
            radioGroup!!.clearCheck()
            radioGroup!!.isEnabled=true
            for (i in 0 until 4) {
                radioGroup!!.getChildAt(i).isEnabled = true
//                val radioButton = radioGroup!!.getChildAt(i) as RadioButton
//                radioButton.isChecked = false

            }

            radioGroup.setOnClickListener(){
                println("click")
                val selectedRadioButtonId = radioGroup?.checkedRadioButtonId

                    val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId!!)
                    val selectedOption = selectedRadioButton.text.toString()
                    println("Selected radio: $selectedOption")

                    // Disable all radio buttons in the group to prevent further selection
                    for (i in 0 until radioGroup!!.childCount) {
                        radioGroup!!.getChildAt(i).isEnabled = false
                    }

            }









        }

}






