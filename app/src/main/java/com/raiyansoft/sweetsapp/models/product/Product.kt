package com.raiyansoft.sweetsapp.models.product

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("desc")
    val desc: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("store_id")
    val store_id: Int,
    @SerializedName("store_name")
    val store_name: String,
    @SerializedName("store_image")
    val store_image: String,
    @SerializedName("category_id")
    val category_id: Int,
    @SerializedName("category_name")
    val category_name: String,
    @SerializedName("offer_percentage")
    val offer: Double
)