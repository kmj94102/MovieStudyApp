package com.example.moviestudyapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviestudyapp.Constants
import com.example.moviestudyapp.data.entity.MyMovie
import com.example.moviestudyapp.databinding.CellBookmarkBinding

class BookmarkAdapter(val clickListener : (Long?) -> Unit) : ListAdapter<MyMovie, BookmarkAdapter.BookmarkViewHolder>(diffUtil) {

    inner class BookmarkViewHolder(val binding : CellBookmarkBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(){
            Glide
                .with(binding.imageView.context)
                .load("${Constants.MOVIE_API_START_IMAGE_URL}${currentList[layoutPosition].backdropPath}")
                .centerCrop()
                .into(binding.imageView)

            binding.imageView.setOnClickListener {
                clickListener(currentList[layoutPosition].movieId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder =
        BookmarkViewHolder(CellBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bind()
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<MyMovie>() {
            override fun areItemsTheSame(oldItem: MyMovie, newItem: MyMovie): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: MyMovie, newItem: MyMovie): Boolean = oldItem.id == newItem.id
        }
    }

}