package com.raiyansoft.sweetsapp.models.appInfo

import com.google.gson.annotations.SerializedName

data class ContactPage(
    @SerializedName("fb")
    val fb: String,
    @SerializedName("insta")
    val insta: String,
    @SerializedName("tel")
    val tel: String,
    @SerializedName("twitter")
    val twitter: String,
    @SerializedName("whatsapp")
    val whatsapp: String
)