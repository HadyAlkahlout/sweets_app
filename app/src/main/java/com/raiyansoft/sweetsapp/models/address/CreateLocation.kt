package com.raiyansoft.sweetsapp.models.address

data class CreateLocation(
    var address: String,
    var lat: String,
    var lng: String,
    val area_id: String,
    val part: String,
    val street: String,
    val avenue: String,
    val house_num: String,
    val floor_num: String,
    val extra_address_note: String
)