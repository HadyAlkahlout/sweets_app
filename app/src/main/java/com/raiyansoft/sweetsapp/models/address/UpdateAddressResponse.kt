package com.raiyansoft.sweetsapp.models.address

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UpdateAddressResponse(
    @SerializedName("address")
    val address: String,
    @SerializedName("address_id")
    val address_id: Int,
    @SerializedName("address_type")
    val address_type: String,
    @SerializedName("area_id")
    val area_id: String,
    @SerializedName("avenue")
    val avenue: String,
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("extra_address_note")
    val extra_address_note: String,
    @SerializedName("house_num")
    val house_num: String,
    @SerializedName("floor_num")
    val floor_num: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lat")
    val lat: String,
    @SerializedName("lng")
    val lng: String,
    @SerializedName("part")
    val part: String,
    @SerializedName("street")
    val street: String,
    @SerializedName("updated_at")
    val updated_at: String,
    @SerializedName("area")
    val area: String
) : Parcelable