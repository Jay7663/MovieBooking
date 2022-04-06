package com.example.moviebooking.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviebooking.R
import com.example.moviebooking.databinding.MovieSeatBinding
import com.example.moviebooking.databinding.MovieSeatHeaderBinding
import com.example.moviebooking.interfaces.SeatOnClickInterface
import com.example.moviebooking.models.MovieSeatData

class MovieSeatAdapter(
    private var context: Context,
    private var movieSeatData: ArrayList<MovieSeatData>,
    private var onSeatOnClickInterface: SeatOnClickInterface,
    private var seatStatus: ArrayList<Boolean>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val viewTypeHeader = 0
    private val viewTypeItem = 1
    var currentSeatStatus = BooleanArray(seatStatus.size)

    inner class ItemViewHolder(val binding: MovieSeatBinding) : RecyclerView.ViewHolder(binding.root)
    inner class SectionHeaderViewHolder(val binding: MovieSeatHeaderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == viewTypeItem) {
            val binding = MovieSeatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ItemViewHolder(binding)
        } else  {
            val binding = MovieSeatHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SectionHeaderViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            viewTypeItem -> {
                val viewHolder = holder as ItemViewHolder
                viewHolder.binding.tvSeatName.text = movieSeatData[position].seatName
                if (seatStatus[position]) {
                    viewHolder.binding.apply {
                        itemMovieSeat.setBackgroundResource(android.R.color.holo_red_dark)
                        tvSeatName.setTextColor(context.resources.getColor(R.color.white))
                    }
                }
                viewHolder.binding.cvMovieSeat.setOnClickListener {
                    if (!seatStatus[position]) {
                        if (currentSeatStatus[position]) {
                            viewHolder.binding.itemMovieSeat.setBackgroundResource(R.drawable.circular_black_stroke)
                        } else {
                            viewHolder.binding.itemMovieSeat.setBackgroundResource(android.R.color.holo_green_light)
                        }
                        currentSeatStatus[position] = !currentSeatStatus[position]
                    }
                    onSeatOnClickInterface.onSeatClicked(getSelectedSeatNames())
                }
            }

            viewTypeHeader -> {
                val viewHolder = holder as SectionHeaderViewHolder
                val currentHeader = movieSeatData[position].headerTitle
                viewHolder.binding.cvSeatHeader.setBackgroundResource(when (currentHeader) {
                    context.getString(R.string.gold) -> {
                        R.color.golden
                    }
                    context.getString(R.string.silver) -> {
                        R.color.silver
                    }
                    context.getString(R.string.bronze) -> {
                        R.color.bronze
                    }
                    else -> {
                        R.color.white
                    }
                })
                viewHolder.binding.tvHeader.text = currentHeader
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (movieSeatData[position].sectionType == context.getString(R.string.header)) {
            viewTypeHeader
        } else {
            viewTypeItem
        }
    }

    override fun getItemCount(): Int {
        return movieSeatData.size
    }

    private fun getSelectedSeatNames(): ArrayList<String> {
        val seatArray: ArrayList<String> = arrayListOf()
        currentSeatStatus.forEachIndexed { index, status ->
            if (status) {
                seatArray.add(movieSeatData[index].seatName.toString())
            }
        }
        return seatArray
    }
}