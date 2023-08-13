package com.yasma.quiz.data

import com.google.gson.annotations.SerializedName

data class question(
    val response_code: Int,

    val results: List<Result>
)