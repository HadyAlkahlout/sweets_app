package com.raiyansoft.sweetsapp.models.store

import com.google.gson.annotations.SerializedName

data class StoreData(
    @SerializedName("store_cover")
    val store_cover: String,
    @SerializedName("store_id")
    val store_id: Int,
    @SerializedName("store_image")
    val store_image: String,
    @SerializedName("store_name")
    val store_name: String
)