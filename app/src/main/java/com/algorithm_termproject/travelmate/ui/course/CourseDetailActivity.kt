package com.algorithm_termproject.travelmate.ui.course

import android.os.Bundle
import com.algorithm_termproject.travelmate.R
import com.algorithm_termproject.travelmate.data.Course
import com.algorithm_termproject.travelmate.data.Place
import com.algorithm_termproject.travelmate.databinding.AcitivityCourseDetailBinding
import com.algorithm_termproject.travelmate.ui.BaseActivity
import com.algorithm_termproject.travelmate.ui.adapter.PlaceRVAdapter
import com.algorithm_termproject.travelmate.utils.addMarker
import com.algorithm_termproject.travelmate.utils.bitMapFromVector
import com.algorithm_termproject.travelmate.utils.drawPolyline
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson


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

        icon = bitMapFromVector(this, R.drawable.ic_pin_d)

        addMarker(map, icon, placeList[0])
        drawPolyline(map, placeList)
        initRV()
    }
}