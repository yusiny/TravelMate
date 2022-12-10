package com.algorithm_termproject.travelmate.ui.main

import com.algorithm_termproject.travelmate.databinding.ActivityMainBinding
import com.algorithm_termproject.travelmate.ui.BaseActivity
import com.algorithm_termproject.travelmate.ui.place.PlaceActivity

class MainActivity: BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun initAfterBinding() {
        binding.mainPlusV.setOnClickListener{
            startNextActivity(PlaceActivity::class.java)
        }
    }
}