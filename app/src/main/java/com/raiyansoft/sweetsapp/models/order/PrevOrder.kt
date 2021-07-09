package com.raiyansoft.sweetsapp.models.order

import com.google.gson.annotations.SerializedName

data class PrevOrder(
    @SerializedName("date")
    val date: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("status")
    val status: String
)