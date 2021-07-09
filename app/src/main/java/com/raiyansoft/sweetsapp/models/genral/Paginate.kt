package com.raiyansoft.sweetsapp.models.genral

import com.google.gson.annotations.SerializedName

data class Paginate(
    @SerializedName("count")
    val count: Int,
    @SerializedName("current_page")
    val current_page: Int,
    @SerializedName("next_page_url")
    val next_page_url: String,
    @SerializedName("per_page")
    val per_page: Int,
    @SerializedName("prev_page_url")
    val prev_page_url: String,
    @SerializedName("total")
    val total: Int,
    @SerializedName("total_pages")
    val total_pages: Int
)