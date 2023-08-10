package com.yasma.quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

const val Base_url="https://jsonplaceholder.typicode.com/"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val text=findViewById<TextView>(R.id.text)

        getmyquestion(text)
    }

    private fun getmyquestion(text: TextView) {
        val retrofitBuilder= Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Base_url)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData=retrofitBuilder.getData()
         retrofitData.enqueue(object : Callback<List<question_dataItem>?> {
             override fun onResponse(
                 call: Call<List<question_dataItem>?>,
                 response: Response<List<question_dataItem>?>
             ) {
                 val responseBody=response.body()!!
                 val stringBuilder=StringBuilder()
                 for (mydata in responseBody){
                     stringBuilder.append(mydata.id)
                     stringBuilder.append(" ")

                 }
                 text.text=stringBuilder
             }

             override fun onFailure(call: Call<List<question_dataItem>?>, t: Throwable) {
                 Toast.makeText(applicationContext," Fail ",Toast.LENGTH_SHORT).show()
             }
         })
    }
}