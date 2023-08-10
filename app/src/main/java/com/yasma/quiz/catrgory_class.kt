package com.yasma.quiz

import com.google.gson.annotations.SerializedName

data class catrgory_class(
    @SerializedName("trivia_categories")
    val trivia_categories: List<TriviaCategory>
)