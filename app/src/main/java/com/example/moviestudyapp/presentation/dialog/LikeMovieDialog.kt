package com.example.moviestudyapp.presentation.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.InputMethodManager
import com.example.moviestudyapp.R
import com.example.moviestudyapp.databinding.DialogLikeMovieBinding

class LikeMovieDialog(context: Context, val okClickListener : (Float, String) -> Unit) : Dialog(context, R.style.DialogBackgroundNull) {

    private val binding : DialogLikeMovieBinding by lazy {
        DialogLikeMovieBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() = with(binding){
        txtRating.text = "0.0"

        ratingBar.setOnRatingChangeListener { _, rating, fromUser ->
            if(fromUser){
                txtRating.text = String.format("%.1f", rating * 2)
            }
        }

        btnOk.setOnClickListener {
            okClickListener(txtRating.text.toString().toFloat(), editMemo.text.toString())
            dismiss()
        }

        btnCancel.setOnClickListener {
            dismiss()
        }

        editMemoLayout.setOnClickListener {
        }
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        Handler(Looper.getMainLooper()).postDelayed({imm.showSoftInput(editMemoLayout, InputMethodManager.SHOW_IMPLICIT)}, 100)

        setCancelable(false)
    }
}