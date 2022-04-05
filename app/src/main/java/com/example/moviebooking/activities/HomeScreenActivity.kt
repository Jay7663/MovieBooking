package com.example.moviebooking.activities


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviebooking.adapters.MoviesAdapter
import com.example.moviebooking.constants.Constants.API_KEY
import com.example.moviebooking.databinding.ActivityHomeScreenBinding
import com.example.moviebooking.interfaces.ApiInterface
import com.example.moviebooking.models.Movie
import com.example.moviebooking.models.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeScreenBinding
    private var moviesList = ArrayList<Result>()
    private lateinit var adapter: MoviesAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var progressBar: ProgressBar
    private lateinit var rvMoviesList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialData()
    }

    private fun initialData() {
        progressBar = binding.progressBar
        rvMoviesList = binding.rvMoviesList
        progressBar.visibility = View.VISIBLE
        getMovies()
    }

    private fun getMovies() {
        val apiInterface = ApiInterface.getRetrofitObject().create(ApiInterface::class.java)
        val retrofitData = apiInterface.getData(API_KEY)

        retrofitData.enqueue(object : Callback<Movie?> {
            override fun onResponse(call: Call<Movie?>, response: Response<Movie?>) {
                val responseBody = response.body()
                if (responseBody != null) {
                    moviesList = responseBody.results as ArrayList<Result>
                }
                runOnUiThread {
                    layoutManager = LinearLayoutManager(applicationContext)
                    rvMoviesList.layoutManager = layoutManager
                    rvMoviesList.setHasFixedSize(true)
                    adapter = MoviesAdapter(moviesList)
                    rvMoviesList.adapter = adapter
                    progressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<Movie?>, t: Throwable) {
                Toast.makeText(this@HomeScreenActivity, "Failed", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }
        })
    }
}