package com.yasma.quiz.data

import com.google.gson.annotations.SerializedName

data class catrgory_class(
    @SerializedName("trivia_categories")
    val trivia_categories: List<TriviaCategory>
)

data class TriviaCategory(
    val id: Int,
    val name: String
)