package com.algorithm_termproject.travelmate.data

import com.google.android.gms.maps.model.LatLng

data class Place(
    val name: String,
    val address: String,
    val latLng: LatLng
)