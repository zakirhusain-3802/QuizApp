package com.yasma.quiz

import com.google.gson.annotations.SerializedName

data class question(
    val response_code: Int,
    @SerializedName("results")
    val results: List<Result>
)