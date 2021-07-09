package com.raiyansoft.sweetsapp.models.home

import com.google.gson.annotations.SerializedName

data class NewProduct(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String
)