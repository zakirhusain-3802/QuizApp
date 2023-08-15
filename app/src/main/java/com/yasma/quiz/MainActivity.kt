package com.yasma.quiz

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.yasma.quiz.data.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.ArrayList

const val Base_url = "https://opentdb.com"
const val QBase_url = "https://opentdb.com/"

class MainActivity : AppCompatActivity() {
    private var catrgory: Int = 0
    private var level: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val text = findViewById<TextView>(R.id.textView)
        val spiner = findViewById<AutoCompleteTextView>(R.id.auto_complete_txt)


        val triviaCategoriesList = listOf(
            TriviaCategory(9, "General Knowledge"),
            TriviaCategory(11, "Entertainment: Film"),
            TriviaCategory(12, "Entertainment: Music"),
            TriviaCategory(17, "Science & Nature"),
            TriviaCategory(18, "Science: Computers"),
            TriviaCategory(21, "Sports"),
            TriviaCategory(22, "Geography"),
            TriviaCategory(23, "History"),
            TriviaCategory(25, "Art"),
            TriviaCategory(27, "Animals"),
            TriviaCategory(29, "Entertainment: Comics"),
            TriviaCategory(30, "Science: Gadgets"),

        )
        val array = ArrayList<String>()
        val datain=ArrayList<TriviaCategory>()
        // Now you can work with the triviaCategoriesList
        for (category in triviaCategoriesList) {
            // Do something with each category, like adding to your datain ArrayList
            val id = category.id
            val name = category.name
            val triviaCategory = TriviaCategory(id, name)
            array.add(name)

            datain.add(triviaCategory)
        }
        val arrac = ArrayAdapter(
            this@MainActivity,
            R.layout.category_list,
            array
        )

        spiner.setAdapter(arrac)
        spiner.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                for (data in datain) {
                    if (data.name.equals(array[p2])) {
                        catrgory = data.id
                        Toast.makeText(
                            applicationContext,
                            " Id od selected category $catrgory",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        val levelSelect = findViewById<AutoCompleteTextView>(R.id.level_drop)
        val startquiz = findViewById<Button>(R.id.startquiz)

        var leves = arrayOf("Easy", "Medium", "Hard")
        val arral = ArrayAdapter(
            this@MainActivity,
            R.layout.category_list,
            leves
        )
        levelSelect.setAdapter(arral)
        levelSelect.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                level = leves[p2].toString()
            }
        }

        startquiz.setOnClickListener {
            if (catrgory != 0 && level != "") {
                change(catrgory, level)
            } else {
                Toast.makeText(
                    applicationContext,
                    " Please select category and level",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun getmyquestion(text: TextView, spiner: AutoCompleteTextView) {

        val retrofitBuilder1 = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Base_url)
            .build()
            .create(ApiInterface::class.java)
        val retrofitData1 = retrofitBuilder1.getCategroy()
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
                        array
                    )

                    spiner.setAdapter(arrap)
                    spiner.onItemClickListener = object : AdapterView.OnItemClickListener {
                        override fun onItemClick(
                            p0: AdapterView<*>?,
                            p1: View?,
                            p2: Int,
                            p3: Long
                        ) {
                            for (data in datain) {
                                if (data.name.equals(array[p2])) {
                                    catrgory = data.id
                                    Toast.makeText(
                                        applicationContext,
                                        " Id od selected category $catrgory",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<catrgory_class?>, t: Throwable) {

            }
        })
    }

    private fun change(catrgory: Int, level: String) {
        val i = Intent(this, question_dis::class.java)
        i.putExtra("cat", catrgory)
        i.putExtra("level", level)
        startActivity(i)
    }
}

data class TriviaCategory(val id: Int, val name: String)

val triviaCategoriesList = listOf(
    TriviaCategory(9, "General Knowledge"),
    TriviaCategory(10, "Entertainment: Books"),
    TriviaCategory(11, "Entertainment: Film"),
    TriviaCategory(12, "Entertainment: Music"),
    TriviaCategory(13, "Entertainment: Musicals & Theatres"),
    TriviaCategory(14, "Entertainment: Television"),
    TriviaCategory(15, "Entertainment: Video Games"),
    TriviaCategory(16, "Entertainment: Board Games"),
    TriviaCategory(17, "Science & Nature"),
    TriviaCategory(18, "Science: Computers"),
    TriviaCategory(19, "Science: Mathematics"),
    TriviaCategory(20, "Mythology"),
    TriviaCategory(21, "Sports"),
    TriviaCategory(22, "Geography"),
    TriviaCategory(23, "History"),
    TriviaCategory(24, "Politics"),
    TriviaCategory(25, "Art"),
    TriviaCategory(26, "Celebrities"),
    TriviaCategory(27, "Animals"),
    TriviaCategory(28, "Vehicles"),
    TriviaCategory(29, "Entertainment: Comics"),
    TriviaCategory(30, "Science: Gadgets"),
    TriviaCategory(31, "Entertainment: Japanese Anime & Manga"),
    TriviaCategory(32, "Entertainment: Cartoon & Animations")
)

// Display the created list


//for(data in datain){
//    if(data.name.equals(array[p2])){
//        catrgory=data.id
//        Toast.makeText(applicationContext," Id od selected category $catrgory",Toast.LENGTH_SHORT).show()
//    }
//}