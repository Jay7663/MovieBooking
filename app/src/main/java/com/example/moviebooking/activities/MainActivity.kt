package com.example.moviebooking.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.moviebooking.constants.Constants
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

        sharedPreferences = getSharedPreferences(Constants.LOGGED_IN_STATUS_PREF_KEY, Context.MODE_PRIVATE)

        lifecycleScope.launch(Dispatchers.IO) {
            delay(Constants.DELAY_TIME)
            val isUserLoggedIn = sharedPreferences.getBoolean("IS_USER_LOGGED_IN", false)

            if (isUserLoggedIn) {
                // Intent directly to home screen
            } else {
                val intent = Intent(this@MainActivity, SignUpActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}