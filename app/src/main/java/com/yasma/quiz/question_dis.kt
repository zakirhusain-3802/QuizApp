package com.yasma.quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//const val QBase_url="https://opentdb.com/"
//https://opentdb.com/api.php?amount=10&category=9&difficulty=medium&type=multiple
class question_dis : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_dis)
        val cat=intent.getIntExtra("cat",0)
        val level=intent.getStringExtra("level")
        println(cat)
        val receivedItemList = intent.getSerializableExtra("cat") as? ArrayList<Result>
        if (receivedItemList != null) {
            // Use the receivedItemList
            println(receivedItemList[1].question)
        }

//        getqustions(cat,level.toString().lowercase())
    }


}