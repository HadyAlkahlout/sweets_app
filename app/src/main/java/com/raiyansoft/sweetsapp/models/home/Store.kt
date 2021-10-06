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
    val offer: Double,
    @SerializedName("min_order_amount")
    val minOrderAmount: Int,
    @SerializedName("fees_amount")
    val feesAmount: Int,
    @SerializedName("fees_duration")
    val feesDuration: Int,
    @SerializedName("rates_stars")
    val ratesStars: Int,
    @SerializedName("is_open")
    val isOpen: Boolean,
    @SerializedName("is_busy")
    val isBusy: Boolean
) : Parcelable