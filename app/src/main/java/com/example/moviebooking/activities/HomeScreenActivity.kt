package com.example.moviebooking.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.moviebooking.R
import com.example.moviebooking.adapters.MoviesAdapter
import com.example.moviebooking.common.Constants
import com.example.moviebooking.common.SharedPreferences
import com.example.moviebooking.databinding.ActivityHomeScreenBinding
import com.example.moviebooking.interfaces.MoviesOnClickInterface
import com.example.moviebooking.viewModel.MovieViewModel
import com.example.moviebooking.models.Result
import com.example.moviebooking.models.UserData

class HomeScreenActivity : AppCompatActivity(), MoviesOnClickInterface {

    private lateinit var binding: ActivityHomeScreenBinding
    private var moviesList = ArrayList<Result>()
    private lateinit var userData: UserData
    private lateinit var adapter: MoviesAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var rvMoviesList: RecyclerView
    private val viewModel: MovieViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userData = intent.extras?.getParcelable("USER_DATA") ?: UserData()
        sharedPreferences = SharedPreferences(getSharedPreferences(Constants.LOGGED_IN_STATUS_PREF_KEY, Context.MODE_PRIVATE))
        supportActionBar?.title = getString(R.string.movie_booking)
        initialData()
    }

    private fun initialData() {
        progressBar = binding.progressBar
        rvMoviesList = binding.rvMoviesList
        progressBar.visibility = View.VISIBLE
        viewModel.getMovies()
        viewModel.allMoviesData.observe(this) { list ->
            rvMoviesList.setHasFixedSize(true)
            moviesList = list
            adapter = MoviesAdapter(list, this@HomeScreenActivity)
            rvMoviesList.adapter = adapter
            progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onMovieItemClicked(position: Int) {
        val intent = Intent(this@HomeScreenActivity, MovieBookingActivity::class.java)
        intent.putExtra("SELECTED_MOVIE", moviesList[position])
        intent.putExtra("USER_DATA", userData)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                sharedPreferences.logOutUser()
                finish()
                startActivity(Intent(this@HomeScreenActivity, LogInActivity::class.java))
                true
            }
            else -> {
                finish()
                super.onOptionsItemSelected(item)
            }
        }
    }
}