package com.algorithm_termproject.travelmate.ui.course

import android.os.Bundle
import com.algorithm_termproject.travelmate.data.Place
import com.algorithm_termproject.travelmate.databinding.ActivityCourseBinding
import com.algorithm_termproject.travelmate.ui.BaseActivity
import com.algorithm_termproject.travelmate.utils.Algorithm
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CourseActivity: BaseActivity<ActivityCourseBinding>(ActivityCourseBinding::inflate) {
    private lateinit var placeList: ArrayList<Place>

    override fun initAfterBinding() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val placeListJson = intent.getStringExtra("placeList")
        val type = object : TypeToken<ArrayList<Place>>() {}.type
        placeList = Gson().fromJson(placeListJson, type)

        val algorithm = Algorithm(placeList)
    }
}