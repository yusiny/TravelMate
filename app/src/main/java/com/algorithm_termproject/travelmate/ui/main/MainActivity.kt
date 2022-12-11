package com.algorithm_termproject.travelmate.ui.main

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.algorithm_termproject.travelmate.R
import com.algorithm_termproject.travelmate.databinding.ActivityMainBinding
import com.algorithm_termproject.travelmate.ui.BaseActivity
import com.algorithm_termproject.travelmate.ui.place.PlaceActivity
import com.algorithm_termproject.travelmate.utils.getName

class MainActivity: BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun initAfterBinding() {
        // Bottom Nav
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment

        navController = navHostFragment.findNavController()

        binding.mainBottomNav.setupWithNavController(navController)
        binding.mainBottomNav.itemIconTintList = null

        // Nickname
        binding.mainNicknameTv.text = getName(this)

        // Click Event
        binding.mainPlusV.setOnClickListener{
            startNextActivity(PlaceActivity::class.java)
        }
    }
}