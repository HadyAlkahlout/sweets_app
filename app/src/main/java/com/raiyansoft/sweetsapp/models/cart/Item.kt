package com.raiyansoft.sweetsapp.models.cart

import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("delivery_fees")
    val delivery_fees: Double,
    @SerializedName("products")
    val products: List<Product>,
    @SerializedName("store_name")
    val store_name: String,
    @SerializedName("cart_id")
    val cart_id: Int
)