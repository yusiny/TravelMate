package com.algorithm_termproject.travelmate.ui.main

import android.content.Intent
import android.util.Log
import com.algorithm_termproject.travelmate.data.Course
import com.algorithm_termproject.travelmate.data.Place
import com.algorithm_termproject.travelmate.databinding.FragmentExploreBinding
import com.algorithm_termproject.travelmate.ui.BaseFragment
import com.algorithm_termproject.travelmate.ui.adapter.CourseRVAdapter
import com.algorithm_termproject.travelmate.ui.course.CourseDetailActivity
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class ExploreFragment: BaseFragment<FragmentExploreBinding>(FragmentExploreBinding::inflate){
    private lateinit var courseRVAdapter: CourseRVAdapter
    private val db = Firebase.firestore

    override fun initAfterBinding() {
        courseRVAdapter = CourseRVAdapter()
        binding.exploreRv.adapter = courseRVAdapter
        courseRVAdapter.setMyItemClickListener(object : CourseRVAdapter.MyClickListener{
            override fun onClick(course: Course) {
                val intent = Intent(requireContext(), CourseDetailActivity::class.java)
                intent.putExtra("course", Gson().toJson(course))

                startActivity(intent)
            }
        })

        getCourses()
    }

    private fun getCourses(){
        val courseList = arrayListOf<Course>()

        db.collection("courses")
            .orderBy("time", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("Explore", "${document.id} => ${document.data}")

                    val user = document.data["user"]
                    val title = document.data["title"]
                    val placeList = document.data["placeList"]

                    val course = Course(user as String, title as String,
                        placeList as ArrayList<Place>
                    )

                    courseList.add(course)
                }
            }
            .addOnCompleteListener {
                courseRVAdapter.addPlaces(courseList)
            }
            .addOnFailureListener { exception ->
                Log.w("Explore", "Error getting documents.", exception)
            }
    }
}