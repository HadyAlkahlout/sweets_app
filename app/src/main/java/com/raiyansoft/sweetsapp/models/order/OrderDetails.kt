package com.raiyansoft.sweetsapp.models.order

import com.google.gson.annotations.SerializedName

data class OrderDetails(
    @SerializedName("address")
    val address: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("delivery_fees")
    val delivery_fees: Double,
    @SerializedName("id")
    val id: Int,
    @SerializedName("items")
    val items: List<OrderItem>,
    @SerializedName("status")
    val status: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("total")
    val total: Double
)