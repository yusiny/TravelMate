package com.algorithm_termproject.travelmate.ui.place

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import com.algorithm_termproject.travelmate.R
import com.algorithm_termproject.travelmate.databinding.ActivityPlaceBinding
import com.algorithm_termproject.travelmate.ui.BaseActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PointOfInterest


class PlaceActivity: BaseActivity<ActivityPlaceBinding>(ActivityPlaceBinding::inflate), OnMapReadyCallback,GoogleMap.OnPoiClickListener {
    private lateinit var map: GoogleMap
    private lateinit var geocoder: Geocoder

    override fun initAfterBinding() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.place_map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        geocoder = Geocoder(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.setOnPoiClickListener(this)
        val SEOUL = LatLng(37.56, 126.97)

        val markerOptions = MarkerOptions()
        markerOptions.position(SEOUL)
        markerOptions.title("서울")
        markerOptions.snippet("한국의 수도")
        map.addMarker(markerOptions)

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 10F));
    }

    override fun onPoiClick(poi: PointOfInterest) {
        Log.d("Place(POI)","Clicked: " +
                poi.name + "\nPlace ID:" + poi.placeId +
                "\nLatitude:" + poi.latLng.latitude +
                " Longitude:" + poi.latLng.longitude)

        Log.d("Place(POI-Geo)", getTextLocation(poi.latLng.latitude, poi.latLng.longitude).toString())
    }

    private fun getTextLocation(lat: Double, lon: Double): String?{
        val list = geocoder.getFromLocation(lat,lon,1);
        var address: String? = null
        if(!list.isNullOrEmpty()) {
            address = "${list[0].postalCode} " + " ${list[0].locality}, ${list[0].adminArea}, ${list[0].countryName} "
        }

        return address;
    }
}