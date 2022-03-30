package com.example.moviebooking.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviebooking.R
import com.example.moviebooking.constants.Constants.IMAGE_BASE_URL
import com.example.moviebooking.models.Result
import java.net.URL
import java.util.concurrent.Executors

class MoviesAdapter(private var movieList: ArrayList<Result>) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_home_screen_movie_item, parent, false)
        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.apply {
            if (movieList[position].posterPath != "") {
                val url = IMAGE_BASE_URL + movieList[position].posterPath
                val executor = Executors.newSingleThreadExecutor()
                val handler = Handler(Looper.getMainLooper())
                var image: Bitmap?

                executor.execute {
                    try {
                        val `in` = URL(url).openStream()
                        image = BitmapFactory.decodeStream(`in`)
                        handler.post {
                            ivImage.setImageBitmap(image)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            tvTitle.text = movieList[position].originalTitle
            tvReleaseDate.text = movieList[position].releaseDate
            rbMovieRatting.rating = movieList[position].voteAverage.toFloat() / 2
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivImage: ImageView = view.findViewById(R.id.ivPoster)
        var tvTitle: TextView = view.findViewById(R.id.tvTitle)
        var tvReleaseDate: TextView = view.findViewById(R.id.tvReleaseDate)
        var rbMovieRatting: RatingBar = view.findViewById(R.id.rbMovieRatting)
    }
}