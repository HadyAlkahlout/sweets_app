package com.raiyansoft.sweetsapp.models.genral

import com.google.gson.annotations.SerializedName

data class GeneralResponse<T> (
    @SerializedName("data")
    val data: T,
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String
)