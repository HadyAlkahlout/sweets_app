package com.raiyansoft.sweetsapp.models.favorite

import com.google.gson.annotations.SerializedName

data class FavProduct(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("desc")
    val desc: String
)