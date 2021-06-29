package com.example.moviestudyapp.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Job

internal abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    abstract val viewModel : VM

    private lateinit var fetchJob : Job

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)

        fetchJob = viewModel.fetchData()
        observeData()

    }

    abstract fun observeData()

    override fun onDestroy() {
        if(fetchJob.isActive){
            fetchJob.cancel()
        }

        super.onDestroy()
    }

}