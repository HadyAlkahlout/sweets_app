package com.raiyansoft.sweetsapp.models.address

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    @SerializedName("address")
    val address: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lat")
    val lat: String,
    @SerializedName("lng")
    val lng: String
) : Parcelable