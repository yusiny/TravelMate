package com.algorithm_termproject.travelmate.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.algorithm_termproject.travelmate.data.Course
import com.algorithm_termproject.travelmate.data.Place
import com.algorithm_termproject.travelmate.databinding.ItemCourseBinding
import com.algorithm_termproject.travelmate.databinding.ItemPlaceBinding

class CourseRVAdapter() : RecyclerView.Adapter<CourseRVAdapter.ViewHolder>() {
    private val courseList = arrayListOf<Course>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(courseList[position])
    }

    override fun getItemCount(): Int = courseList.size


    inner class ViewHolder(private val binding: ItemCourseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(course: Course){
            binding.itemCourseTitleTv.text = course.title
            binding.itemCourseNameTv.text = course.user

            binding.root.setOnClickListener {
                myClickListener.onClick(course)
            }
        }
    }

    /* 데이터 */

    fun addPlaces(courses: ArrayList<Course>){
        courseList.clear()
        courseList.addAll(courses)
        notifyDataSetChanged()
    }

    /* Change Listener */
    private lateinit var myClickListener: MyClickListener

    interface MyClickListener {
        fun onClick(course: Course)
    }

    fun setMyItemClickListener(myClickListener: MyClickListener) {
        this.myClickListener = myClickListener
    }
}