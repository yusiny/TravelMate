package com.algorithm_termproject.travelmate.ui.place

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import com.algorithm_termproject.travelmate.R
import com.algorithm_termproject.travelmate.data.Place
import com.algorithm_termproject.travelmate.databinding.ActivityPlaceBinding
import com.algorithm_termproject.travelmate.ui.BaseActivity
import com.algorithm_termproject.travelmate.ui.course.CourseActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.algorithm_termproject.travelmate.utils.removeEnter
import com.algorithm_termproject.travelmate.utils.getTextLocation
import com.algorithm_termproject.travelmate.utils.bitMapFromVector


class PlaceActivity : BaseActivity<ActivityPlaceBinding>(ActivityPlaceBinding::inflate),
    OnMapReadyCallback, GoogleMap.OnPoiClickListener {
    private lateinit var map: GoogleMap
    private lateinit var geocoder: Geocoder
    private var markerList = arrayListOf<Marker>()
    private lateinit var icon: BitmapDescriptor

    private lateinit var placeRVAdapter: PlaceRVAdapter

    override fun initAfterBinding() {
        initRV()
        setClickListener()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.place_map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        geocoder = Geocoder(this)
    }

    /* Map */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.56, 126.97), 10F))

        icon = bitMapFromVector(this, R.drawable.ic_pin)
        map.setOnPoiClickListener(this)
    }

    override fun onPoiClick(poi: PointOfInterest) {
        Log.d(
            "Place(POI)", "Clicked: " +
                    poi.name + "\nPlace ID:" + poi.placeId +
                    "\nLatitude:" + poi.latLng.latitude +
                    " Longitude:" + poi.latLng.longitude
        )

        Log.d(
            "Place(POI-Geo)",
            getTextLocation(geocoder, poi.latLng.latitude, poi.latLng.longitude).toString()
        )

        val address = getTextLocation(geocoder, poi.latLng.latitude, poi.latLng.longitude).toString()
        showDialog(removeEnter(poi.name), address, poi.latLng)
    }


    /* Function */
    private fun initRV() {
        placeRVAdapter = PlaceRVAdapter()
        binding.placePlacesRv.adapter = placeRVAdapter

        placeRVAdapter.setMyItemClickListener(object : PlaceRVAdapter.MyItemChangedListener {
            override fun onItemAdded(place: Place, size: Int) {
                if (size > 0) binding.placeNoneTv.visibility = View.GONE
                addMarker(place)
            }

            override fun onItemDeleted(index: Int, place: Place, size: Int) {
                if (size == 0) binding.placeNoneTv.visibility = View.VISIBLE
                deleteMarker()
            }

        })
    }

    private fun setClickListener() {
        binding.placeFinishTv.setOnClickListener {
            val placeList = placeRVAdapter.getPlaceList()

            Log.d("Place(List)", placeList.toString())

            if (placeList.size < 2){
                showToast("장소를 2개 이상 선택해 주세요")
                return@setOnClickListener
            }

            val intent = Intent(this, CourseActivity::class.java)
            val placeListJson = Gson().toJson(placeList)
            intent.putExtra("placeList", placeListJson)

            startActivity(intent)
            finish()
        }
    }

    private fun showDialog(name: String, address: String, latLng: LatLng) {
        val dialog = NewPlaceDialog()
        dialog.setMyDialogCallback(object : NewPlaceDialog.MyDialogCallback {
            override fun add(place: Place) {
                placeRVAdapter.addPlace(place)
                addMarker(place)
            }

        })

        val bundle = Bundle()
        val place = Place(name, address, latLng)
        bundle.putString("place", Gson().toJson(place))

        dialog.arguments = bundle
        dialog.show(supportFragmentManager, null)
    }

    private fun addMarker(place: Place) {
        val marker = map.addMarker(
            MarkerOptions()
                .position(place.latLng)
                .title(place.name)
                .icon(icon)
        )

        if (marker != null) {
            markerList.add(marker)
        }
    }

    private fun deleteMarker(){
        map.clear()

        val placeList = placeRVAdapter.getPlaceList()
        for(place in placeList){
            addMarker(place)
        }
    }

}