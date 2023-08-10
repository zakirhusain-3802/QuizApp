package com.yasma.quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

const val Base_url="https://opentdb.com"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val text=findViewById<TextView>(R.id.text)
        //https://opentdb.com/api.php?amount=10&category=9&difficulty=easy&type=multiple

        getmyquestion(text)
    }

    private fun getmyquestion(text: TextView) {
        val retrofitBuilder= Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Base_url)
            .build()
            .create(ApiInterface::class.java)
//        val retrofitData=retrofitBuilder.getCategroy()
//        retrofitData.enqueue(object : Callback<List<TriviaCategory>?> {
//            override fun onResponse(
//                call: Call<List<TriviaCategory>?>,
//                response: Response<List<TriviaCategory>?>
//            ) {
//                val responseBody=response.body()!!
//                val triviaCategoriesList = ArrayList<TriviaCategory>()
//
//                for (mydata in responseBody) {
//                    val id = mydata.id
//                    val name = mydata.name
//                    val triviaCategory = TriviaCategory(id, name)
//                    triviaCategoriesList.add(triviaCategory)
//                }
//                text.text=triviaCategoriesList.toString()
//
//            }
//
//            override fun onFailure(call: Call<List<TriviaCategory>?>, t: Throwable) {
//                Toast.makeText(applicationContext,"Error",Toast.LENGTH_SHORT).show()
//            }
//        })

        val retrofitBuilder1= Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Base_url)
            .build()
            .create(ApiInterface::class.java)
        val retrofitData1=retrofitBuilder1.getCategroy()
        retrofitData1.enqueue(object : Callback<catrgory_class?> {
            override fun onResponse(
                call: Call<catrgory_class?>,
                response: Response<catrgory_class?>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    val triviaCategoriesList = responseBody.trivia_categories
                    val datain=ArrayList<TriviaCategory>()

                    // Now you can work with the triviaCategoriesList
                    for (category in triviaCategoriesList) {
                        // Do something with each category, like adding to your datain ArrayList
                        val id=category.id
                        val name=category.name
                        val triviaCategory = TriviaCategory(id, name)

                        datain.add(triviaCategory)
                    }
                    println(datain)
                    text.text=datain.toString()
                }

            }

            override fun onFailure(call: Call<catrgory_class?>, t: Throwable) {

            }
        })



//        val retrofitData=retrofitBuilder.getData()
//         retrofitData.enqueue(object : Callback<List<question_dataItem>?> {
//             override fun onResponse(
//                 call: Call<List<question_dataItem>?>,
//                 response: Response<List<question_dataItem>?>
//             ) {
//                 val responseBody=response.body()!!
//                 val stringBuilder=StringBuilder()
//                 for (mydata in responseBody){
//                     stringBuilder.append(mydata.id)
//                     stringBuilder.append(" ")
//
//                 }
//                 text.text=stringBuilder
//             }
//
//             override fun onFailure(call: Call<List<question_dataItem>?>, t: Throwable) {
//                 Toast.makeText(applicationContext," Fail ",Toast.LENGTH_SHORT).show()
//             }
//         })
    }
}