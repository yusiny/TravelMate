package com.algorithm_termproject.travelmate.ui.course

import android.os.Bundle
import android.util.Log
import com.algorithm_termproject.travelmate.R
import com.algorithm_termproject.travelmate.data.Course
import com.algorithm_termproject.travelmate.data.Place
import com.algorithm_termproject.travelmate.databinding.ActivityCourseBinding
import com.algorithm_termproject.travelmate.ui.BaseActivity
import com.algorithm_termproject.travelmate.ui.adapter.PlaceRVAdapter
import com.algorithm_termproject.travelmate.utils.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class CourseActivity: BaseActivity<ActivityCourseBinding>(ActivityCourseBinding::inflate),
    OnMapReadyCallback {
    private lateinit var placeList: ArrayList<Place>
    private lateinit var path: ArrayList<Place>

    private lateinit var map: GoogleMap
    private lateinit var icon: BitmapDescriptor
    private lateinit var cameraPosition: CameraPosition

    private val db = Firebase.firestore

    override fun initAfterBinding() {
        binding.courseSaveTv.setOnClickListener {
            val title = binding.courseTitleEt.text.toString()
            val course = Course(getName(this), title, path)
            uploadCourse(course)
        }

        binding.courseTitleEt.setText(getName(this) + "의 코스")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val placeListJson = intent.getStringExtra("placeList")
        val type = object : TypeToken<ArrayList<Place>>() {}.type
        placeList = Gson().fromJson(placeListJson, type)

        cameraPosition = Gson().fromJson(intent.getStringExtra("camera"), CameraPosition::class.java)

        val algorithm = Algorithm(placeList)
        path = algorithm.path

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.course_map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    private fun initRV(){
        val rvAdapter = PlaceRVAdapter("none")
        binding.coursePlacesRv.adapter = rvAdapter
        rvAdapter.addPlaces(path)
    }

    /* Map */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val latLng = LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, cameraPosition.zoom - 0.5f))

        icon = bitMapFromVector(this, R.drawable.ic_pin_d)
        addMarker(map, icon, path[0])
        drawPolyline(map, path)

        initRV()
    }

    /* Firebase */
    private fun uploadCourse(course: Course){
        val course = hashMapOf(
            "user" to course.user,
            "title" to course.title,
            "placeList" to course.placeList,
            "time" to System.currentTimeMillis()
        )

        db.collection("courses")
            .add(course)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "Firebase",
                    "DocumentSnapshot added with ID: " + documentReference.id
                )
                finish()
            }
            .addOnFailureListener { e -> Log.w("Firebase", "Error adding document", e) }
    }
}