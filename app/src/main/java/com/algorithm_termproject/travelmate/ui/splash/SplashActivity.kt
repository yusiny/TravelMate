package com.algorithm_termproject.travelmate.ui.splash

import com.algorithm_termproject.travelmate.databinding.ActivitySplashBinding
import com.algorithm_termproject.travelmate.ui.BaseActivity
import com.algorithm_termproject.travelmate.ui.main.MainActivity
import com.algorithm_termproject.travelmate.utils.saveName

class SplashActivity: BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    override fun initAfterBinding() {
        binding.splashGoTv.setOnClickListener {
            val name = binding.splashNameEt.text.toString()
            saveName(this, name)
            startNextActivity(MainActivity::class.java)
            finish()
        }

    }
}