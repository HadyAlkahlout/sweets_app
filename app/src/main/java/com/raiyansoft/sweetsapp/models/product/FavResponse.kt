package com.raiyansoft.sweetsapp.models.product

import com.google.gson.annotations.SerializedName

data class FavResponse(
    @SerializedName("status")
    val status: Boolean
)