package com.example.moviebooking.common

import android.content.SharedPreferences

class SharedPreferences(private val sharedPreferences: SharedPreferences) {
    private val editor = sharedPreferences.edit()

    fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(Constants.USER_LOGIN_KEY, false)
    }

    fun setUserLoggedIn(userId: String) {
        editor.putString(Constants.USER_ID_KEY, userId)
        editor.putBoolean(Constants.USER_LOGIN_KEY, true)
        editor.apply()
    }

    fun logOutUser() {
        editor.clear()
        editor.apply()
    }
}