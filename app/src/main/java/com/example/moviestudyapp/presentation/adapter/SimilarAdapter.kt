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
import com.example.moviestudyapp.network.SimilarList
import com.example.moviestudyapp.network.SimilarListResult
import com.example.moviestudyapp.network.TrendingList

class SimilarAdapter(private val context: Context, val trendingSelectListener : (Long?) -> Unit) : ListAdapter<SimilarList, SimilarAdapter.SimilarViewHolder>(diffUtil){

    inner class SimilarViewHolder(val binding : CellSimilarBinding) : RecyclerView.ViewHolder(binding.root){

        init {
            binding.root.setOnClickListener {
                trendingSelectListener(currentList[layoutPosition].id)
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

    fun currentItem(position: Int) = currentList[position]

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<SimilarList>(){

            override fun areItemsTheSame(oldItem: SimilarList, newItem: SimilarList): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: SimilarList, newItem: SimilarList): Boolean  = oldItem.id == newItem.id

        }
    }

}