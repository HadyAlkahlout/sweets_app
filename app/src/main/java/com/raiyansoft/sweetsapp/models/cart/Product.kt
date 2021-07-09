package com.raiyansoft.sweetsapp.models.cart

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("qty")
    val qty: Int,
    @SerializedName("total")
    val total: Double
)