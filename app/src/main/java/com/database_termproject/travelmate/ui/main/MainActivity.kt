package com.database_termproject.travelmate.ui.main

import com.database_termproject.travelmate.databinding.ActivityMainBinding
import com.database_termproject.travelmate.ui.BaseActivity
import com.database_termproject.travelmate.ui.place.PlaceActivity

class MainActivity: BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun initAfterBinding() {
        binding.mainPlusV.setOnClickListener{
            startNextActivity(PlaceActivity::class.java)
        }
    }
}