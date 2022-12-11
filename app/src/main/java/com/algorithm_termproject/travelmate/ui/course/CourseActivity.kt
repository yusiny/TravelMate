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
import com.algorithm_termproject.travelmate.utils.getName
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.p72b.maps.animation.AnimatedPolyline


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
            val course = Course(getName(this), title, placeList)
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
        rvAdapter.addPlaces(placeList)
    }

    /* Map */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val latLng = LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, cameraPosition.zoom - 0.5f))

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

        val latLngList = arrayListOf<LatLng>()
        for(place in placeList){
            latLngList.add(place.latLng)
        }
        latLngList.add(placeList[0].latLng)

        val animatedPolyline = AnimatedPolyline(map, latLngList, polylineOptions)

        //polylineOptions.add(placeList[0].latLng)
       // val polyline = map.addPolyline(polylineOptions)

        animatedPolyline.start()
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