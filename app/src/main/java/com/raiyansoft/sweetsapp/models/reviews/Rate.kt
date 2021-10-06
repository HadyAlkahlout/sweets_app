package com.raiyansoft.sweetsapp.models.reviews

import com.google.gson.annotations.SerializedName

data class Rate(
    @SerializedName("rate")
    val rate: Double,
    @SerializedName("text")
    val text: String,
    @SerializedName("user_name")
    val user_name: String
)