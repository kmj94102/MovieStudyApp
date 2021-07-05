package com.example.moviestudyapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviestudyapp.Constants
import com.example.moviestudyapp.databinding.CellSimilarBinding
import com.example.moviestudyapp.network.MovieSearchList
import com.example.moviestudyapp.network.SimilarList
import com.example.moviestudyapp.network.SimilarListResult
import com.example.moviestudyapp.network.TrendingList

class SearchMovieAdapter(private val context: Context, val searchMovieSelectListener : (Long?) -> Unit) : ListAdapter<MovieSearchList, SearchMovieAdapter.SimilarViewHolder>(diffUtil){

    inner class SimilarViewHolder(private val binding : CellSimilarBinding) : RecyclerView.ViewHolder(binding.root){

        init {
            binding.root.setOnClickListener {
                searchMovieSelectListener(currentList[layoutPosition].id)
            }
        }

        fun bind() {
            Glide.with(context).load("${Constants.MOVIE_API_START_IMAGE_URL}${currentList[layoutPosition].backdrop_path}").centerCrop().into(binding.imgSimilar)
            binding.txtSimilarTitle.text = currentList[layoutPosition].title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarViewHolder =
        SimilarViewHolder(CellSimilarBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: SimilarViewHolder, position: Int) {
        holder.bind()
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<MovieSearchList>(){

            override fun areItemsTheSame(oldItem: MovieSearchList, newItem: MovieSearchList): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: MovieSearchList, newItem: MovieSearchList): Boolean  = oldItem.id == newItem.id

        }
    }

}