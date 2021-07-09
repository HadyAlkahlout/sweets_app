package com.raiyansoft.sweetsapp.models.genral

import com.google.gson.annotations.SerializedName

data class GeneralPaginateResponse<T>(
    @SerializedName("data")
    val `data`: Data<T>,
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String
)