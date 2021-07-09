package com.raiyansoft.sweetsapp.models.product

import com.google.gson.annotations.SerializedName

data class Gallery(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String
)