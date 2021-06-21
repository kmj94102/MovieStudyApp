package com.example.moviestudyapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviestudyapp.databinding.CellTrendingBinding

class TrendingAdapter(private val context: Context, private val list : List<String>) : RecyclerView.Adapter<TrendingAdapter.TrendingViewHolder>() {
    private var listener : View.OnClickListener? = null

    inner class TrendingViewHolder(binding : CellTrendingBinding) : RecyclerView.ViewHolder(binding.root){
        var imageView: ImageView = binding.imageView

        init {
            binding.root.setOnClickListener {
                it.tag = adapterPosition
                listener?.onClick(it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingViewHolder =
        TrendingViewHolder(CellTrendingBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: TrendingViewHolder, position: Int) {
        Glide.with(context).load(list[position]).centerCrop().into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setListener(listener : View.OnClickListener){
        this.listener = listener
    }
}