package com.raiyansoft.sweetsapp.models.home

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Store(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("cover")
    val cover: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("offer")
    val offer: Double
) : Parcelable