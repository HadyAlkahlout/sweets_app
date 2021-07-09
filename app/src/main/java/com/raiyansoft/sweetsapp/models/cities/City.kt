package com.raiyansoft.sweetsapp.models.cities

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("governorate_name")
    val name: String,
    @SerializedName("areas")
    val regions: List<Region>
)