package com.raiyansoft.sweetsapp.models.filter

import com.google.gson.annotations.SerializedName

data class FilterData(
    @SerializedName("categories")
    val categories: List<Data>,
    @SerializedName("occasions")
    val occasions: List<Data>,
    @SerializedName("preparations")
    val preparations: List<Data>
)