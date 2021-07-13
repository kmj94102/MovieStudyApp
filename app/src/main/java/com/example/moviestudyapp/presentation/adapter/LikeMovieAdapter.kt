package com.example.moviestudyapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviestudyapp.Constants
import com.example.moviestudyapp.data.entity.MyMovie
import com.example.moviestudyapp.databinding.CellLikeMovieBinding

class LikeMovieAdapter(val clickListener : (Long?) -> Unit ) : ListAdapter<MyMovie, LikeMovieAdapter.LikeMovieViewHolder>(diffUtil) {

    inner class LikeMovieViewHolder(private val binding : CellLikeMovieBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                clickListener(currentList[layoutPosition].movieId)
            }
        }

        fun bind(){
            Glide.with(binding.imageView.context).load("${Constants.MOVIE_API_START_IMAGE_URL}${currentList[layoutPosition].backdropPath}").centerCrop().into(binding.imageView)
            binding.txtMovieTitle.text = "${currentList[layoutPosition].title}"
            binding.txtMyAverage.text = "${currentList[layoutPosition].myVoteAverage ?: 0.0}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeMovieViewHolder =
        LikeMovieViewHolder(CellLikeMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: LikeMovieViewHolder, position: Int) {
        holder.bind()
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<MyMovie>(){
            override fun areItemsTheSame(oldItem: MyMovie, newItem: MyMovie): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: MyMovie, newItem: MyMovie): Boolean = oldItem.id == newItem.id

        }
    }
}
