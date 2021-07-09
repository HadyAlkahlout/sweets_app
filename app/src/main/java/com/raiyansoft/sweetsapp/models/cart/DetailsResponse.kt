package com.raiyansoft.sweetsapp.models.cart

import com.google.gson.annotations.SerializedName

data class DetailsResponse(
    @SerializedName("show_payment_gateway")
    val showPaymentGateway: String
)