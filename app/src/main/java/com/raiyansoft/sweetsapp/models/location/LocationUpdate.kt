package com.raiyansoft.sweetsapp.models.location

import com.google.gson.annotations.SerializedName

data class LocationUpdate(
    @SerializedName("address")
    val address: String,
    @SerializedName("address_id")
    val address_id: Int,
    @SerializedName("area_id")
    val area_id: String,
    @SerializedName("address_type")
    val address_type: String,
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lat")
    val lat: String,
    @SerializedName("lng")
    val lng: String,
    @SerializedName("updated_at")
    val updated_at: String
)