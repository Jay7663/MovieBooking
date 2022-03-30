package com.example.moviebooking.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.moviebooking.R
import com.example.moviebooking.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            validateAndSignUpUser()
        }

        binding.tvLogin.setOnClickListener {
            startLoginActivity()
        }
    }

    private fun startLoginActivity() {
        val intent = Intent(this@SignUpActivity, LogInActivity::class.java)
        startActivity(intent)
    }

    private fun validateAndSignUpUser() {
        val email = binding.tfEmail.text.toString().trim()
        val password = binding.tfPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this@SignUpActivity, getString(R.string.invalid_credentials), Toast.LENGTH_SHORT).show()
        } else {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, getString(R.string.signup_successful), Toast.LENGTH_SHORT).show()
                    startLoginActivity()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
}