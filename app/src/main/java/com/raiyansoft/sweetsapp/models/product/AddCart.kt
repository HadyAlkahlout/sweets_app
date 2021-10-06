package com.raiyansoft.sweetsapp.models.product

import com.google.gson.annotations.SerializedName
import com.raiyansoft.sweetsapp.models.cart.PassOption

data class AddCart(
    @SerializedName("dedicate_message")
    val dedicate_message: String = "",
    @SerializedName("dedicate_price")
    val dedicate_price: Double = 0.0,
    @SerializedName("qty")
    val qty: Int = 1,
    @SerializedName("additions")
    val additions: ArrayList<PassOption> = arrayListOf()
)