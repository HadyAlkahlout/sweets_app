package com.raiyansoft.sweetsapp.models.home

import com.google.gson.annotations.SerializedName

data class HomeResponse(
    @SerializedName("ads")
    val ads: List<Ad>,
    @SerializedName("categories")
    val categories: List<Category>,
    @SerializedName("most_sales")
    val most_sales: List<NewProduct>,
    @SerializedName("new_products")
    val new_products: List<NewProduct>,
    @SerializedName("occasions")
    val occasions: List<Category>,
    @SerializedName("stores")
    val stores: List<Store>
)