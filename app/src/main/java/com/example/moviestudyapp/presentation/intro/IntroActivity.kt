package com.example.moviestudyapp.presentation.intro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.moviestudyapp.MainActivity
import com.example.moviestudyapp.R
import org.jetbrains.anko.startActivity

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity<MainActivity>()
            finish()
        }, 2000)

    }
}