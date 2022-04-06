package com.example.moviebooking.interfaces

import com.example.moviebooking.models.UserData

interface FirebaseInterface {
    fun onSuccess(userData: UserData?)
    fun onFailure(exception: String)
}