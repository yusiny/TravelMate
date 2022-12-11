package com.algorithm_termproject.travelmate.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

/* LatLag -> Location */
fun getTextLocation(geocoder: Geocoder, lat: Double, lon: Double): String? {
    val list = geocoder.getFromLocation(lat, lon, 1);
    var address: String? = null
    if (!list.isNullOrEmpty()) {
        address =
            "${list[0].postalCode} " + " ${list[0].locality}, ${list[0].adminArea}, ${list[0].countryName} "
        address = removeNull(address)
    }

    return address;
}

/* Vector to bitMap*/
fun bitMapFromVector(context: Context, redId:Int): BitmapDescriptor {
    val vectorDrawable= ContextCompat.getDrawable(context,redId)
    vectorDrawable!!.setBounds(0,0,vectorDrawable.intrinsicWidth,vectorDrawable.intrinsicHeight)
    val bitmap=
        Bitmap.createBitmap(vectorDrawable.intrinsicWidth,vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888)
    val canvas= Canvas(bitmap)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

/* String */
fun removeEnter(str: String): String{
    return str.replace("\n", " ")
}

fun removeNull(str: String): String{
    return str.replace("null, ", "")
}

