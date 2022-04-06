package com.example.moviebooking.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.moviebooking.common.Constants
import com.example.moviebooking.common.SharedPreferences
import com.example.moviebooking.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = SharedPreferences(getSharedPreferences(Constants.LOGGED_IN_STATUS_PREF_KEY, Context.MODE_PRIVATE))
        val imageAnimation = AnimationUtils.loadAnimation(this@MainActivity ,com.bumptech.glide.R.anim.abc_slide_in_bottom)
        val titleAnimation = AnimationUtils.loadAnimation(this@MainActivity ,com.bumptech.glide.R.anim.abc_grow_fade_in_from_bottom)
        binding.splashScreenImage.startAnimation(imageAnimation)
        binding.tvAppTitle.startAnimation(titleAnimation)

        lifecycleScope.launch(Dispatchers.IO) {
            delay(Constants.DELAY_TIME)
            val isUserLoggedIn = sharedPreferences.isUserLoggedIn()

            if (isUserLoggedIn) {
                val intent = Intent(this@MainActivity, HomeScreenActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@MainActivity, LogInActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}