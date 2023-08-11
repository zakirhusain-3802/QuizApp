package com.yasma.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Array
import kotlin.collections.ArrayList

const val Base_url="https://opentdb.com"

class MainActivity : AppCompatActivity() {
   private var catrgory:Int = 0
    private var level:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val text=findViewById<TextView>(R.id.textView)
        val spiner=findViewById<AutoCompleteTextView>(R.id.auto_complete_txt)
        val levelSelect=findViewById<AutoCompleteTextView>(R.id.level_drop)
        val startquiz=findViewById<Button>(R.id.startquiz)

        var leves = arrayOf("Easy", "Medium", "Hard")
        val arrap = ArrayAdapter(
            this@MainActivity,
            R.layout.category_list,
            leves)
        levelSelect.setAdapter(arrap)
        levelSelect.onItemClickListener=object :AdapterView.OnItemClickListener{
            override fun onItemClick(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {

                level=leves[p2].toString()

            }

        }

        startquiz.setOnClickListener(){
            if(catrgory!=0&&level!=""){

            text.text=catrgory.toString()+"  "+level.toString()
                
                val i =Intent(this,question_dis::class.java)
                startActivity(i)
            }
            else{

                text.text="Plze select category nd level"
            }
        }




        getmyquestion(text,spiner)

    }

    private fun getmyquestion(text: TextView, spiner:AutoCompleteTextView) {

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
                    val datain = ArrayList<TriviaCategory>()
                    val array = ArrayList<String>()

                    // Now you can work with the triviaCategoriesList
                    for (category in triviaCategoriesList) {
                        // Do something with each category, like adding to your datain ArrayList
                        val id = category.id
                        val name = category.name
                        val triviaCategory = TriviaCategory(id, name)
                        array.add(name)

                        datain.add(triviaCategory)
                    }
                    println(datain)
//                    text.text=datain.toString()
                    val arrap = ArrayAdapter(
                        this@MainActivity,
                        R.layout.category_list,
                        array)

                    spiner.setAdapter(arrap)
                    spiner.onItemClickListener=object :AdapterView.OnItemClickListener{
                        override fun onItemClick(
                            p0: AdapterView<*>?,
                            p1: View?,
                            p2: Int,
                            p3: Long
                        ) {
                            for(data in datain){
                                if(data.name.equals(array[p2])){
                                    catrgory=data.id
                                    Toast.makeText(applicationContext," Id od selected category $catrgory",Toast.LENGTH_SHORT).show()
                                }
                            }

                        }

                    }

                }}


            override fun onFailure(call: Call<catrgory_class?>, t: Throwable) {

            }
        })

    }
    }


//for(data in datain){
//    if(data.name.equals(array[p2])){
//        catrgory=data.id
//        Toast.makeText(applicationContext," Id od selected category $catrgory",Toast.LENGTH_SHORT).show()
//    }
//}