package com.raiyansoft.sweetsapp.models.store

import com.google.gson.annotations.SerializedName
import com.raiyansoft.sweetsapp.models.product.Product

data class StoreCategory(
    @SerializedName("category_id")
    val category_id: Int,
    @SerializedName("category_name")
    val category_name: String,
    @SerializedName("products")
    val products: List<Product>
)