package com.example.moviebooking.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.activity.viewModels
import com.example.moviebooking.R
import com.example.moviebooking.common.Constants
import com.example.moviebooking.common.SharedPreferences
import com.example.moviebooking.databinding.ActivityLogInBinding
import com.example.moviebooking.interfaces.FirebaseInterface
import com.example.moviebooking.models.UserData
import com.example.moviebooking.viewModel.MovieViewModel
import com.vdx.designertoast.DesignerToast

class LogInActivity : AppCompatActivity(), FirebaseInterface {

    private lateinit var binding: ActivityLogInBinding
    private val viewModel: MovieViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.login)
        sharedPreferences = SharedPreferences(getSharedPreferences(Constants.LOGGED_IN_STATUS_PREF_KEY, Context.MODE_PRIVATE))

        binding.btnLogin.setOnClickListener {
            validateLoginCredentials()
        }

        binding.tvSignUp.setOnClickListener {
            startSignupActivity()
        }
    }

    private fun startSignupActivity() {
        val intent = Intent(this@LogInActivity, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateLoginCredentials() {
        val email = binding.tfEmail.text.toString().trim()
        val password = binding.tfPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            DesignerToast.Warning(this, getString(R.string.invalid_credentials), Gravity.BOTTOM, Toast.LENGTH_SHORT)
        } else {
            viewModel.signInUser(email, password, this)
        }
    }

    override fun onSuccess(userData: UserData?) {
        var loggedInUserId = ""
        viewModel.loggedInUserId.value?.let {
            loggedInUserId = it
        }
        if (binding.cbRememberMe.isChecked) {
            sharedPreferences.setUserLoggedIn(loggedInUserId)
        }
        val intent = Intent(this@LogInActivity,HomeScreenActivity::class.java)
        intent.putExtra("USER_DATA", userData)
        startActivity(intent)
        finish()
    }

    override fun onFailure(exception: String) {
        DesignerToast.Error(this, exception, Gravity.BOTTOM, Toast.LENGTH_SHORT)
    }
}