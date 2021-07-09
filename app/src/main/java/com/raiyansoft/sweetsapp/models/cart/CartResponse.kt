package com.raiyansoft.sweetsapp.models.cart

import com.google.gson.annotations.SerializedName

data class CartResponse(
    @SerializedName("items")
    val items: List<Item>,
    @SerializedName("total")
    val total: Double,
    @SerializedName("total_delivery_fees")
    val total_delivery_fees: Double
)