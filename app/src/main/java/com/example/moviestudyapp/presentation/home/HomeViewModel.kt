package com.example.moviestudyapp.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moviestudyapp.domain.GetTrendingMovieListUseCase
import com.example.moviestudyapp.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class HomeViewModel(
    private val getTrendingMovieListUseCase: GetTrendingMovieListUseCase
) : BaseViewModel(){

    private var _homeLiveData = MutableLiveData<HomeState>(HomeState.UnInitialized)
    val homeLiveData : LiveData<HomeState> = _homeLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        Log.e("++++++", "fetchData")
        _homeLiveData.postValue(HomeState.Loading)
        _homeLiveData.postValue(HomeState.Success(getTrendingMovieListUseCase(MEDIA_TYPE, TIME_WINDOW)))
    }

    companion object{
        const val MEDIA_TYPE = "movie"
        const val TIME_WINDOW = "day"
    }
}