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
import com.example.moviebooking.databinding.ActivitySignUpBinding
import com.example.moviebooking.interfaces.FirebaseInterface
import com.example.moviebooking.models.UserData
import com.example.moviebooking.viewModel.MovieViewModel
import com.vdx.designertoast.DesignerToast

class SignUpActivity : AppCompatActivity(), FirebaseInterface {

    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: MovieViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = SharedPreferences(getSharedPreferences(Constants.LOGGED_IN_STATUS_PREF_KEY, Context.MODE_PRIVATE))
        supportActionBar?.title = getString(R.string.signup)

        binding.btnSignUp.setOnClickListener {
            validateAndSignUpUser()
        }

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this@SignUpActivity, LogInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun validateAndSignUpUser() {
        val firstName = binding.etFirstName.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()
        val email = binding.tfEmail.text.toString().trim()
        val password = binding.tfPassword.text.toString().trim()

        if (firstName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            DesignerToast.Warning(this, getString(R.string.invalid_credentials), Gravity.BOTTOM, Toast.LENGTH_SHORT)
        } else {
            viewModel.createUser(firstName,lastName, email, password, this)
        }
    }

    override fun onSuccess(userData: UserData?) {
        var loggedInUserId = ""
        viewModel.loggedInUserId.value?.let {
            loggedInUserId = it
        }
        sharedPreferences.setUserLoggedIn(loggedInUserId)
        val intent = Intent(this@SignUpActivity, HomeScreenActivity::class.java)
        intent.putExtra("USER_DATA", userData)
        startActivity(intent)
        finish()
    }

    override fun onFailure(exception: String) {
        DesignerToast.Error(this, exception, Gravity.BOTTOM, Toast.LENGTH_SHORT)
    }
}