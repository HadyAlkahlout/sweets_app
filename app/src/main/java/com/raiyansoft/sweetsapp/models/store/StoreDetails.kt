package com.raiyansoft.sweetsapp.models.store

import com.google.gson.annotations.SerializedName

data class StoreDetails(
    @SerializedName("store_data")
    val store_data: StoreData
)