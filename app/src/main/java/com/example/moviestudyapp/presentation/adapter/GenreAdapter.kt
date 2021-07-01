package com.example.moviestudyapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviestudyapp.R

class GenreAdapter(private val list: List<String>) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {
    inner class GenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtGenre: TextView = itemView.findViewById(R.id.txt_genre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        return GenreViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_genre, parent, false))
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.txtGenre.text = list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }
}