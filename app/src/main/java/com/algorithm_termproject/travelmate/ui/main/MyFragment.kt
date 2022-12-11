package com.algorithm_termproject.travelmate.ui.main

import android.content.Intent
import android.util.Log
import com.algorithm_termproject.travelmate.data.Course
import com.algorithm_termproject.travelmate.data.Place
import com.algorithm_termproject.travelmate.databinding.FragmentMyBinding
import com.algorithm_termproject.travelmate.ui.BaseFragment
import com.algorithm_termproject.travelmate.ui.adapter.CourseRVAdapter
import com.algorithm_termproject.travelmate.ui.course.CourseDetailActivity
import com.algorithm_termproject.travelmate.utils.getName
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class MyFragment: BaseFragment<FragmentMyBinding>(FragmentMyBinding::inflate){
    private lateinit var courseRVAdapter: CourseRVAdapter
    private val db = Firebase.firestore

    override fun initAfterBinding() {
        courseRVAdapter = CourseRVAdapter()
        binding.myRv.adapter = courseRVAdapter
        courseRVAdapter.setMyItemClickListener(object : CourseRVAdapter.MyClickListener{
            override fun onClick(course: Course) {
                val intent = Intent(requireContext(), CourseDetailActivity::class.java)
                intent.putExtra("course", Gson().toJson(course))

                startActivity(intent)
            }
        })

        getMyCourses()
    }

    private fun getMyCourses(){
        val courseList = arrayListOf<Course>()

        db.collection("courses")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("Explore", "${document.id} => ${document.data}")

                    val user = document.data["user"]
                    val title = document.data["title"]
                    val placeList = document.data["placeList"]

                    if(user != getName(requireContext())) return@addOnSuccessListener

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