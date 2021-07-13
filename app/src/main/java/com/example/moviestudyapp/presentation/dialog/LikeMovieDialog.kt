package com.example.moviestudyapp.presentation.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import com.example.moviestudyapp.databinding.DialogLikeMovieBinding

class LikeMovieDialog(context: Context, val okClickListener : (Float, String) -> Unit) : AlertDialog(context) {

    private val binding : DialogLikeMovieBinding by lazy {
        DialogLikeMovieBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window?.setBackgroundDrawableResource(android.R.color.transparent)

        initViews()
    }

    private fun initViews() = with(binding){
        ratingBar.setOnRatingBarChangeListener { _, rating, fromUser ->
            if(fromUser){
                txtRating.text = "${rating * 2}"
            }
        }

        btnOk.setOnClickListener {
            okClickListener(txtRating.text.toString().toFloat(), editMemo.text.toString())
            dismiss()
        }
    }
}