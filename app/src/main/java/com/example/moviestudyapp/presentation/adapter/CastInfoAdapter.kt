package com.example.moviestudyapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviestudyapp.Constants
import com.example.moviestudyapp.R
import com.example.moviestudyapp.databinding.CellCastInfoBinding
import com.example.moviestudyapp.network.CastInfo
import org.jetbrains.anko.backgroundColorResource

class CastInfoAdapter(val itemClickListener : (String?, Long?) -> Unit) : ListAdapter<CastInfo,CastInfoAdapter.CastInfoViewHolder>(diffUtil) {

    inner class CastInfoViewHolder(val binding : CellCastInfoBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind() = with(binding){
            binding.root.setOnClickListener {
                itemClickListener(currentList[layoutPosition].original_name, currentList[layoutPosition].id)
            }

            if(currentList[layoutPosition].profile_path.isNullOrEmpty()) {
                imgCast.backgroundColorResource = R.color.dark_color
            }else {
                Glide.with(imgCast.context).load("${Constants.MOVIE_API_START_IMAGE_URL}${currentList[layoutPosition].profile_path}").centerCrop().into(imgCast)
            }
            txtActorName.text = currentList[layoutPosition].name
            txtCastName.text = currentList[layoutPosition].character

            txtActorName.isSelected = true
            txtCastName.isSelected = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastInfoViewHolder {
        return CastInfoViewHolder(CellCastInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CastInfoViewHolder, position: Int) {
        holder.bind()
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<CastInfo>(){
            override fun areItemsTheSame(oldItem: CastInfo, newItem: CastInfo): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: CastInfo, newItem: CastInfo): Boolean = oldItem.id == newItem.id
        }
    }
}
