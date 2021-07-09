package com.raiyansoft.sweetsapp.models.filter

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    var selected: Boolean
)