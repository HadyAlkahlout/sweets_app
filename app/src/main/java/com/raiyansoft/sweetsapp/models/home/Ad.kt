package com.raiyansoft.sweetsapp.models.home

import com.google.gson.annotations.SerializedName

data class Ad(
    @SerializedName("external_url")
    val external_url: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("type_id")
    val type_id: Int
)