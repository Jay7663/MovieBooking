package com.example.moviebooking.models

data class MovieSeatData(
    val sectionType: String,
    val headerTitle: String? = null,
    val seatStatus: Boolean,
    val seatName: String? = null
)
