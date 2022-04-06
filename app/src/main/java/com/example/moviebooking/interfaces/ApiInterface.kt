package com.example.moviebooking.interfaces

import com.example.moviebooking.models.Movie
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("3/movie/popular")
    fun getData(@Query("api_key", encoded = false) key: String, @Query("page", encoded = false) pageNumber: Int = 1): Call<Movie>

    companion object {
        fun getRetrofitObject(): Retrofit {
            return Retrofit.Builder().baseUrl("https://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}