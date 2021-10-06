package com.raiyansoft.sweetsapp.models.product

import com.google.gson.annotations.SerializedName

data class Addition(
    @SerializedName("multiple")
    val multiple: List<Option>,
    @SerializedName("once")
    val once: List<Option>
)