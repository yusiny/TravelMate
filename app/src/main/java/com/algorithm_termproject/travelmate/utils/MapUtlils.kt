package com.algorithm_termproject.travelmate.utils

import android.graphics.Color
import com.algorithm_termproject.travelmate.data.Place
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import de.p72b.maps.animation.AnimatedPolyline

/* Google Map Utils */

/** A function to draw polyline with animation */
fun drawPolyline(map: GoogleMap, placeList: ArrayList<Place>){
    val polylineOptions = PolylineOptions().width(25f)
        .color(Color.parseColor("#3771E0"))
        .geodesic(true)

    val latLngList = arrayListOf<LatLng>()
    for(place in placeList){
        latLngList.add(place.latLng)
    }
    latLngList.add(placeList[0].latLng)

    val animatedPolyline = AnimatedPolyline(map, latLngList, polylineOptions)

    animatedPolyline.start()
}

/** A function to add marker on given place */
fun addMarker(map: GoogleMap, icon: BitmapDescriptor, place: Place) {
    val marker = map.addMarker(
        MarkerOptions()
            .position(place.latLng)
            .title(place.name)
            .icon(icon)
    )
}

/** A function to replace markers in given placeList */
fun deleteMarkers(map: GoogleMap, icon: BitmapDescriptor, placeList: ArrayList<Place>){
    map.clear()

    for(place in placeList){
        addMarker(map, icon, place)
    }
}