package com.raiyansoft.sweetsapp.models.reviews

import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("can_rate")
    val can_rate: Boolean,
    @SerializedName("rate_count")
    val rate_count: Int,
    @SerializedName("rates")
    val rates: List<Rate>
)