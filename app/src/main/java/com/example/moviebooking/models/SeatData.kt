package com.example.moviebooking.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SeatData(
    val bookedSeatArray: List<String>? = listOf()
): Parcelable
