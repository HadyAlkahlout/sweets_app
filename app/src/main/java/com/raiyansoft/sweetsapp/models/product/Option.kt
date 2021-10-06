package com.raiyansoft.sweetsapp.models.product

import com.google.gson.annotations.SerializedName

data class Option(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("type")
    val type: String,
    var isSelected: Boolean = false
)