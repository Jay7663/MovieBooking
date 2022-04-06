package com.example.moviebooking.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserData(
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null
): Parcelable