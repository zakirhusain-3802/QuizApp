package com.yasma.quiz

import retrofit2.Call
import retrofit2.http.GET


interface ApiInterface {
    @GET("posts")
    fun getData(): Call<List<question_dataItem>>
}