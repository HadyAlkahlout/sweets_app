package com.raiyansoft.sweetsapp.models.home

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("expire_at")
    val expire_at: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String
)