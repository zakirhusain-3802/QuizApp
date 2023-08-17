package com.yasma.quiz

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.os.postDelayed
import com.yasma.quiz.data.que_ans
import com.yasma.quiz.data.question
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

//const val QBase_url="https://opentdb.com/"
//https://opentdb.com/api.php?amount=10&category=9&difficulty=medium&type=multiple
class question_dis : AppCompatActivity() {
    private var s: Int = 0
    private var total: Int = 0
    private lateinit var countDownTimer: CountDownTimer
    private val totalTime: Long = 20000 // 60 seconds in milliseconds
    private val interval: Long = 5// 1 second in milliseconds
    private lateinit var mediaPlayer: MediaPlayer
    private val datain = ArrayList<que_ans>()

    private val handler = Handler()
    private val initialDelay: Long = 1000
    private val timeInterval: Long = 24000 // Time interval in milliseconds (e.g., 10 seconds)


    private val initialRunnable: Runnable = object : Runnable {
        override fun run() {
            showProgressBar()
            // Call your initial function here
            val cat = intent.getIntExtra("cat", 0)
            val level = intent.getStringExtra("level")

            getqustions(cat, level.toString().lowercase())

            // After initial delay, start the repeated execution
//            hideProgressBar()
            handler.postDelayed(runnable, 5000)
        }
    }

    private fun hideProgressBar() {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar1)
        progressBar.visibility = View.INVISIBLE
        val card = findViewById<CardView>(R.id.cardView)
        card.visibility = View.VISIBLE

    }

    private fun showProgressBar() {
        val card = findViewById<CardView>(R.id.cardView)
        card.visibility = View.INVISIBLE
        val progressBar = findViewById<ProgressBar>(R.id.progressBar1)
        progressBar.visibility = View.VISIBLE

    }


    private val runnable: Runnable = object : Runnable {
        override fun run() {
            // Call your function here
            if (s != 10) {

                myFunction()


                // Re-post the runnable after the specified time interval
                handler.postDelayed(this, timeInterval)
            }
            else{
                onBackPressed()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Countdown duration in milliseconds

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_dis)
        showProgressBar()
//        showProgressBar()
//        val cat=intent.getIntExtra("cat",0)
//        val level=intent.getStringExtra("level")

//        getqustions(cat,level.toString().lowercase())
        handler.postDelayed(initialRunnable, initialDelay)

//        startCountdown()
    }


    private fun getqustions(cat: Int, level: String?) {
        val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(QBase_url)
            .build()
            .create(ApiInterface::class.java)
        val retfofitData =
            retrofitBuilder.getquestion(10, cat, level.toString().lowercase(), "multiple")
        retfofitData.enqueue(object : Callback<question?> {
            override fun onResponse(call: Call<question?>, response: Response<question?>) =
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val qeustion = responseBody?.results
                    println(responseBody!!.response_code.toString() + " que")

                    for (data in qeustion!!) {
                        val q = data.question
                        val ans = data.correct_answer
                        val wrg = data.incorrect_answers
                        val queandans = que_ans(q.toString(), ans.toString(), wrg)
                        datain.add(queandans)


                    }
                    println(datain + " try")

                } else {
                    // Handle the error case, such as logging an error message
                    println("Error: ${response.code()} - ${response.message()}")
                }

            override fun onFailure(call: Call<question?>, t: Throwable) {
                println(t.message.toString())
                Toast.makeText(
                    applicationContext,
                    "Please check internet connetivty",
                    Toast.LENGTH_SHORT
                ).show()
                s = 10
//                onBackPressed()

            }
        })
    }

    private fun startCountdown() {
        mediaPlayer = MediaPlayer.create(this, R.raw.count_down1)

        countDownTimer = object : CountDownTimer(totalTime, interval) {
            override fun onTick(millisUntilFinished: Long) {
                val progress = (millisUntilFinished.toFloat() / totalTime * 100).toInt()
                val progressBar = findViewById<ProgressBar>(R.id.progressBar)
                progressBar.progress = progress
                val secondsLeft = millisUntilFinished / 1000
                val timeLeftTextView = findViewById<TextView>(R.id.timeLeftTextView)
                timeLeftTextView.text = "Time Left: $secondsLeft s"
                if (secondsLeft == 12L) {
                    mediaPlayer.start()
                }

            }

            override fun onFinish() {
                // Countdown has finished, you can perform actions here
                val progressBar = findViewById<ProgressBar>(R.id.progressBar)
                progressBar.progress = 0
                val timeLeftTextView = findViewById<TextView>(R.id.timeLeftTextView)
                timeLeftTextView.text = "Time Left: 0 s"
            }
        }

        // Start the countdown timer when the activity starts
        countDownTimer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel the countdown timer to prevent memory leaks
        if(::countDownTimer.isInitialized){
            countDownTimer.cancel()
        }

    }

    private fun myFunction() {

        hideProgressBar()
        startCountdown()
        // Your function's code here
        val qno = findViewById<TextView>(R.id.qno)
        val question: String = datain[s].question
        val corret: String = datain[s].correct_answer
        val incorect = datain[s].incorrect_answers
        val option = ArrayList<String>()
        for (data in incorect) {
            val o = data.toString()
            option.add(o.toString())
        }
        println(corret.toString() + " Right")
        option.add(corret.toString())
        Collections.shuffle(option)
        println(option)
        var points = findViewById<TextView>(R.id.points)
        var selectoption: RadioButton? = null
        val qutxt = findViewById<TextView>(R.id.questionTextView)
        val A = findViewById<RadioButton>(R.id.optionARadioButton)
        val B = findViewById<RadioButton>(R.id.optionBRadioButton)
        val C = findViewById<RadioButton>(R.id.optionCRadioButton)
        val D = findViewById<RadioButton>(R.id.optionDRadioButton)
        s = s + 1
        qno.text = "Q $s/10"
        qutxt.text = question
        A.setBackgroundResource(R.drawable.radio_selector)
        A.setTextColor(Color.BLACK)
        B.setBackgroundResource(R.drawable.radio_selector)
        B.setTextColor(Color.BLACK)
        C.setBackgroundResource(R.drawable.radio_selector)
        C.setTextColor(Color.BLACK)
        D.setBackgroundResource(R.drawable.radio_selector)
        D.setTextColor(Color.BLACK)
        A.text = option[0]
        B.text = option[1]
        C.text = option[2]
        D.text = option[3]

        val radioGroup = findViewById<RadioGroup>(R.id.optionsRadioGroup)
        radioGroup!!.clearCheck()
        radioGroup!!.isEnabled = true
        for (i in 0 until 4) {
            radioGroup!!.getChildAt(i).isEnabled = true
//                val radioButton = radioGroup!!.getChildAt(i) as RadioButton
//                radioButton.isChecked = false

        }
        A.setOnClickListener() {
            val selectedRadioButtonId = radioGroup?.checkedRadioButtonId

            val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId!!)
            val selectedOption = selectedRadioButton.text.toString()
            A.setTextColor(Color.WHITE)
            selectoption = A

            println("Selected radio: $selectedOption")

            // Disable all radio buttons in the group to prevent further selection
            for (i in 0 until radioGroup!!.childCount) {
                radioGroup!!.getChildAt(i).isEnabled = false
            }
        }
        B.setOnClickListener() {
            val selectedRadioButtonId = radioGroup?.checkedRadioButtonId

            val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId!!)
            val selectedOption = selectedRadioButton.text.toString()
            B.setTextColor(Color.WHITE)
            selectoption = B

            println("Selected radio: $selectedOption")

            // Disable all radio buttons in the group to prevent further selection
            for (i in 0 until radioGroup!!.childCount) {
                radioGroup!!.getChildAt(i).isEnabled = false
            }
        }
        C.setOnClickListener() {
            val selectedRadioButtonId = radioGroup?.checkedRadioButtonId

            val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId!!)
            C.setTextColor(Color.WHITE)
            val selectedOption = selectedRadioButton.text.toString()
            selectoption = C

            println("Selected radio: $selectedOption")

            // Disable all radio buttons in the group to prevent further selection
            for (i in 0 until radioGroup!!.childCount) {
                radioGroup!!.getChildAt(i).isEnabled = false
            }
        }
        D.setOnClickListener() {
            val selectedRadioButtonId = radioGroup?.checkedRadioButtonId

            val selectedRadioButton = findViewById<RadioButton>(R.id.optionDRadioButton)
            D.setTextColor(Color.WHITE)
            val selectedOption = selectedRadioButton.text.toString()
            selectoption = D

            println("Selected radio: $selectedOption")

            // Disable all radio buttons in the group to prevent further selection
            for (i in 0 until radioGroup!!.childCount) {
                radioGroup!!.getChildAt(i).isEnabled = false
            }
        }
        handler.postDelayed({
            if (selectoption != null) {
                if (selectoption!!.text.toString().equals(corret)) {
                    selectoption!!.setBackgroundResource(R.color.green)
                    selectoption!!.setTextColor(Color.WHITE)
                    total = total + 10
                    points.text = total.toString() + "/100"
                    showDialog()


                } else {
                    selectoption!!.setBackgroundResource(R.color.red)
                    selectoption!!.setTextColor(Color.WHITE)
                    if (A.text.toString().equals(corret)) {
                        A.setBackgroundResource(R.color.green)
                        A.setTextColor(Color.WHITE)

                    } else if (B.text.toString().equals(corret)) {
                        B.setBackgroundResource(R.color.green)
                        B.setTextColor(Color.WHITE)

                    } else if (C.text.toString().equals(corret)) {
                        C.setBackgroundResource(R.color.green)
                        C.setTextColor(Color.WHITE)

                    } else if (D.text.toString().equals(corret)) {
                        D.setBackgroundResource(R.color.green)
                        D.setTextColor(Color.WHITE)

                    }
                    wrongDialog()

                }

            } else {
                if (A.text.toString().equals(corret)) {
                    A.setBackgroundResource(R.color.green)
                    A.setTextColor(Color.WHITE)

                } else if (B.text.toString().equals(corret)) {
                    B.setBackgroundResource(R.color.green)
                    B.setTextColor(Color.WHITE)

                } else if (C.text.toString().equals(corret)) {
                    C.setBackgroundResource(R.color.green)
                    C.setTextColor(Color.WHITE)

                } else if (D.text.toString().equals(corret)) {
                    D.setBackgroundResource(R.color.green)
                    D.setTextColor(Color.WHITE)

                }
                wrongDialog()


            }

        }, 20000)


        radioGroup.setOnClickListener() {
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

//    override fun onBackPressed() {
//        super.onBackPressed()
//
////        val i = Intent(this, MainActivity::class.java)
////
////        startActivity(i)
//    }

    private fun showDialog() {
        val customDialog = AlertDialog.Builder(this)
            .setView(R.layout.custom_dailog) // Set your custom layout here
            .create()

        customDialog.show()
        handler.postDelayed({
            customDialog.dismiss()
        }, 3800)

    }


    private fun wrongDialog() {

        val customDialog = AlertDialog.Builder(this)
            .setView(R.layout.custom_wrong) // Set your custom layout here
            .create()

        customDialog.show()
        handler.postDelayed({
            customDialog.dismiss()
        },3800)
//        customDialog.dismiss()

    }
}









