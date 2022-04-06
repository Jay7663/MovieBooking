package com.example.moviebooking.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.moviebooking.R
import com.example.moviebooking.adapters.MovieSeatAdapter
import com.example.moviebooking.common.Constants.IMAGE_BASE_URL
import com.example.moviebooking.databinding.ActivityMovieBookingBinding
import com.example.moviebooking.interfaces.SeatOnClickInterface
import com.example.moviebooking.models.MovieSeatData
import com.example.moviebooking.models.Result
import com.example.moviebooking.models.UserData
import com.example.moviebooking.viewModel.MovieViewModel

class MovieBookingActivity : AppCompatActivity(), SeatOnClickInterface {

    private lateinit var binding: ActivityMovieBookingBinding
    private lateinit var movieSeatData: ArrayList<MovieSeatData>
    private lateinit var seatStatus: ArrayList<Boolean>
    private lateinit var userData: UserData
    private lateinit var adapter: MovieSeatAdapter
    private val viewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialiseMovieData()
        userData = intent.extras?.getParcelable("USER_DATA") ?: UserData()
        val selectedMovie = intent.extras?.getParcelable<Result>("SELECTED_MOVIE")
        val layoutManager = GridLayoutManager(this, 10)

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter.getItemViewType(position) == adapter.viewTypeHeader) 10 else 1
            }
        }

        binding.apply {
            selectedMovie?.let {
                Glide.with(this@MovieBookingActivity).load(IMAGE_BASE_URL + it.posterPath).into(ivMovieImage)
                tvMovieName.text = it.originalTitle
                tvMovieOverview.text = it.overview
                viewModel.getSeatStatus(it.originalTitle) { bookedSeatsArray ->
                    seatStatus = arrayListOf()
                    bookedSeatsArray?.let {
                        movieSeatData.forEachIndexed { _, movieSeatData ->
                            if (movieSeatData.seatName != null) {
                                seatStatus.add(bookedSeatsArray.contains(movieSeatData.seatName))
                            } else {
                                seatStatus.add(false)
                            }
                        }
                    }
                    adapter = MovieSeatAdapter(this@MovieBookingActivity, movieSeatData,this@MovieBookingActivity, seatStatus)
                    rvGridOfSeats.layoutManager = layoutManager
                    rvGridOfSeats.adapter = adapter
                }
            }
        }

        binding.btnSubmitTicket.setOnClickListener {

        }
    }

    private fun initialiseMovieData() {
        movieSeatData = arrayListOf()
        val seatNames = getSeatNames()
        for (data in 0..62) {
            when (data) {
                0 -> {
                    movieSeatData.add(MovieSeatData(getString(R.string.header), getString(R.string.gold),false))
                }
                21 -> {
                    movieSeatData.add(MovieSeatData(getString(R.string.header), getString(R.string.silver),false))
                }
                42 -> {
                    movieSeatData.add(MovieSeatData(getString(R.string.header), getString(R.string.bronze),false))
                }
                else -> {
                    movieSeatData.add(MovieSeatData(getString(R.string.item), null,false, seatNames[data]))
                }
            }
        }
    }

    private fun getSeatNames(): ArrayList<String> {
        val seatNameArray: ArrayList<String> = arrayListOf()
        var seatIndex = 0
        for (index in 0..62) {
            when (index) {
                0, 21, 42 -> {
                    seatNameArray.add(getString(R.string.header))
                }
                in 1..10 -> {
                    seatNameArray.add(getString(R.string.tier_a_seat_name, seatIndex))
                }
                in 11..20 -> {
                    seatNameArray.add(getString(R.string.tier_b_seat_name, seatIndex))
                }
                in 22..31 -> {
                    seatNameArray.add(getString(R.string.tier_c_seat_name, seatIndex))
                }
                in 32..41 -> {
                    seatNameArray.add(getString(R.string.tier_d_seat_name, seatIndex))
                }
                in 43..52 -> {
                    seatNameArray.add(getString(R.string.tier_e_seat_name, seatIndex))
                }
                in 53..62 -> {
                    seatNameArray.add(getString(R.string.tier_f_seat_name, seatIndex))
                }
            }
            if (index != 0 && index != 21 && index != 42) {
                seatIndex += 1
                seatIndex %= 10
            }
        }
        return seatNameArray
    }

    override fun onSeatClicked(seatArray: ArrayList<String>) {
        Log.d("Selected seat", seatArray.toString())
    }
}