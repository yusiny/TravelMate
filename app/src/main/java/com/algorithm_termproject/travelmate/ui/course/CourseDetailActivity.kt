package com.algorithm_termproject.travelmate.ui.course

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.algorithm_termproject.travelmate.R
import com.algorithm_termproject.travelmate.data.Course
import com.algorithm_termproject.travelmate.data.Place
import com.algorithm_termproject.travelmate.databinding.AcitivityCourseDetailBinding
import com.algorithm_termproject.travelmate.databinding.ActivityCourseBinding
import com.algorithm_termproject.travelmate.ui.BaseActivity
import com.algorithm_termproject.travelmate.ui.adapter.PlaceRVAdapter
import com.algorithm_termproject.travelmate.utils.Algorithm
import com.algorithm_termproject.travelmate.utils.bitMapFromVector
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class CourseDetailActivity :
    BaseActivity<AcitivityCourseDetailBinding>(AcitivityCourseDetailBinding::inflate),
    OnMapReadyCallback {
    private lateinit var rvAdapter: PlaceRVAdapter

    private lateinit var map: GoogleMap
    private lateinit var icon: BitmapDescriptor

    private lateinit var course: Course
    private lateinit var placeList: ArrayList<Place>

    override fun initAfterBinding() {
        binding.courseDetailBackIv.setOnClickListener {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val courseJson = intent.getStringExtra("course")
        course = Gson().fromJson(courseJson, Course::class.java)
        placeList = course.placeList
        initUI()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.course_detail_map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    private fun initRV() {
        rvAdapter = PlaceRVAdapter("none")
        binding.courseDetailPlacesRv.adapter = rvAdapter
        rvAdapter.addPlaces(placeList)
    }

    private fun initUI() {
        binding.courseDetailTitleTv.text = course.title
        binding.courseDetailUserTv.text = course.user
    }

    /* Map */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(placeList[0].latLng, 12F))

        icon = bitMapFromVector(this, R.drawable.ic_pin)

        drawPolyLine()
        initRV()
    }

    private fun drawPolyLine() {
        val polylineOptions = PolylineOptions().width(25f)
            .color(Color.parseColor("#3771E0"))
            .geodesic(true)

        for (place in placeList) {
            polylineOptions.add(place.latLng)
        }

        polylineOptions.add(placeList[0].latLng)

        val polyline = map.addPolyline(polylineOptions)
    }
}