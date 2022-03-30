package com.example.moviebooking.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.moviebooking.R
import com.example.moviebooking.constants.Constants
import com.example.moviebooking.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth

class LogInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.login)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        sharedPreferences = getSharedPreferences(Constants.LOGGED_IN_STATUS_PREF_KEY, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        binding.btnLogin.setOnClickListener {
            validateLoginCredentials()
        }

        binding.tvSignUp.setOnClickListener {
            finish()
        }
    }

    private fun validateLoginCredentials() {
        val email = binding.tfEmail.text.toString().trim()
        val password = binding.tfPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this@LogInActivity, getString(R.string.invalid_credentials), Toast.LENGTH_SHORT).show()
        } else {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (binding.cbRememberMe.isChecked) {
                        editor.putBoolean("IS_USER_LOGGED_IN", true)
                        editor.apply()
                    }
                    Toast.makeText(this, getString(R.string.login_successful), Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}