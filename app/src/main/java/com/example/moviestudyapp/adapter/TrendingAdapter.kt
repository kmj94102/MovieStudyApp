package com.example.moviestudyapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviestudyapp.Constants
import com.example.moviestudyapp.databinding.CellTrendingBinding
import com.example.moviestudyapp.network.TrendingListResult

class TrendingAdapter(private val context: Context, private var trendingList : List<TrendingListResult>, val trendingSelectListener : (Long?) -> Unit) : RecyclerView.Adapter<TrendingAdapter.TrendingViewHolder>() {

    inner class TrendingViewHolder(binding : CellTrendingBinding) : RecyclerView.ViewHolder(binding.root){
        var imageView: ImageView = binding.imageView

        init {
            binding.root.setOnClickListener {
                trendingSelectListener(trendingList[adapterPosition].id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingViewHolder =
        TrendingViewHolder(CellTrendingBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: TrendingViewHolder, position: Int) {
        Glide.with(context).load("${Constants.MOVIE_API_START_IMAGE_URL}${trendingList[position].backdrop_path}").centerCrop().into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return trendingList.size
    }

    fun addTrendingList(trendingList : List<TrendingListResult>) {
        this.trendingList = trendingList
        notifyDataSetChanged()
    }

    fun currentItem(position: Int) = trendingList[position]

}