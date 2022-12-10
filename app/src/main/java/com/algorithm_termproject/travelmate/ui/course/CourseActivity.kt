package com.algorithm_termproject.travelmate.ui.course

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.algorithm_termproject.travelmate.R
import com.algorithm_termproject.travelmate.data.Course
import com.algorithm_termproject.travelmate.data.Place
import com.algorithm_termproject.travelmate.databinding.ActivityCourseBinding
import com.algorithm_termproject.travelmate.ui.BaseActivity
import com.algorithm_termproject.travelmate.ui.adapter.PlaceRVAdapter
import com.algorithm_termproject.travelmate.utils.Algorithm
import com.algorithm_termproject.travelmate.utils.bitMapFromVector
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
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

    private val db = Firebase.firestore

    override fun initAfterBinding() {
        //databaseRef = FirebaseDatabase.getInstance().reference

        binding.courseSaveTv.setOnClickListener {
            val title = binding.courseTitleEt.text.toString()
            val course = Course("yusin", title, placeList)
            uploadCourse(course)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val placeListJson = intent.getStringExtra("placeList")
        val type = object : TypeToken<ArrayList<Place>>() {}.type
        placeList = Gson().fromJson(placeListJson, type)

        val algorithm = Algorithm(placeList)
        path = algorithm.path

        Log.d("CourseActivity", path.toString())

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.course_map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    private fun initRV(){
        val rvAdapter = PlaceRVAdapter("none")
        binding.coursePlacesRv.adapter = rvAdapter
        rvAdapter.addPlaces(placeList)
    }

    /* Map */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(placeList[0].latLng, 10F))

        icon = bitMapFromVector(this, R.drawable.ic_pin)
        drawPolyLine()

        initRV()
    }

    private fun addMarkers(){
        for(place in placeList){
            map.addMarker(
                MarkerOptions()
                    .position(place.latLng)
                    .title(place.name)
                    .icon(icon)
            )
        }
    }

    private fun drawPolyLine(){
        val polylineOptions = PolylineOptions().width(25f)
            .color(Color.parseColor("#3771E0"))
            .geodesic(true)

        for(place in placeList){
            polylineOptions.add(place.latLng)
        }

        polylineOptions.add(placeList[0].latLng)

        val polyline = map.addPolyline(polylineOptions)
    }

    /* Firebase */
    private fun uploadCourse(course: Course){
        val user = hashMapOf(
            "user" to course.user,
            "title" to course.title,
            "placeList" to course.placeList
        )

        db.collection("courses")
            .add(user)
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