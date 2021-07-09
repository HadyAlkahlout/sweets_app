package com.raiyansoft.sweetsapp.models.cities

import com.google.gson.annotations.SerializedName

data class Region(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)