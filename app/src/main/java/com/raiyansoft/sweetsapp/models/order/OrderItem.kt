package com.raiyansoft.sweetsapp.models.order

import com.google.gson.annotations.SerializedName

data class OrderItem(
    @SerializedName("products")
    val products: List<OrderProduct>,
    @SerializedName("store_name")
    val store_name: String
)