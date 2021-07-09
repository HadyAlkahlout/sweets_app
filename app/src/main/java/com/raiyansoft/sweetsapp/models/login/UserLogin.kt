package com.raiyansoft.sweetsapp.models.login

import com.google.gson.annotations.SerializedName

data class UserLogin(
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("deleted_at")
    val deleted_at: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("updated_at")
    val updated_at: String,
    @SerializedName("verification_code")
    val verification_code: Int,
    @SerializedName("sex")
    var sex: String,
    @SerializedName("birth_date")
    var birth_date: String
)