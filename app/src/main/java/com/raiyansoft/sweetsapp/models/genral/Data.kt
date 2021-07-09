package com.raiyansoft.sweetsapp.models.genral

import com.google.gson.annotations.SerializedName

data class Data<T>(
    @SerializedName("data")
    val `data`: List<T>,
    @SerializedName("paginate")
    val paginate: Paginate
)