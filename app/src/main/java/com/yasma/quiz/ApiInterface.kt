package com.yasma.quiz

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiInterface {
    @GET(value = "posts")
    fun getData(): Call<List<question_dataItem>>
    @GET(value="/api_category.php")
    fun getCategroy():Call<catrgory_class>
    @GET("api.php")
    fun getquestion(

        @Query("amount") amount: Int,
        @Query("category") category: Int,
        @Query("difficulty") difficulty: String,
        @Query("type") type: String
    ):Call<question>
}