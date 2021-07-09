package com.raiyansoft.sweetsapp.models.profile

import com.google.gson.annotations.SerializedName

data class UpdateProfile(
    @SerializedName("name")
    var name: String,
    @SerializedName("phone")
    var phone: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("sex")
    var sex: String,
    @SerializedName("birth_date")
    var birth_date: String
)