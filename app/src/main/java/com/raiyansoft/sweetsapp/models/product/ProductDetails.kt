package com.raiyansoft.sweetsapp.models.product

import com.google.gson.annotations.SerializedName
import com.raiyansoft.sweetsapp.models.home.NewProduct

data class ProductDetails(
    @SerializedName("desc")
    val desc: String,
    @SerializedName("gallery")
    val gallery: List<Gallery>,
    @SerializedName("have_dedicate")
    val have_dedicate: Int,
    @SerializedName("dedicate_price")
    val dedicate_price: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("preparation_minute")
    val preparation_minute: Int,
    @SerializedName("price")
    val price: Double,
    @SerializedName("similier_products")
    val similier_products: List<NewProduct>,
    @SerializedName("favorite")
    val favourite: Int,
    @SerializedName("offer_percentage")
    val offer: Double
)