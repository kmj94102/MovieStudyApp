package com.example.moviestudyapp.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moviestudyapp.domain.GetSimilarMovieListUseCase
import com.example.moviestudyapp.domain.GetTrendingMovieListUseCase
import com.example.moviestudyapp.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

internal class HomeViewModel(
    var movieId : Long?,
    private val getTrendingMovieListUseCase: GetTrendingMovieListUseCase,
    private val getSimilarMovieListUseCase: GetSimilarMovieListUseCase
) : BaseViewModel(){

    private var _homeLiveData = MutableLiveData<HomeState>(HomeState.UnInitialized)
    val homeLiveData : LiveData<HomeState> = _homeLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        _homeLiveData.postValue(HomeState.Loading)
        try {
            val trendingList = getTrendingMovieListUseCase(MEDIA_TYPE, TIME_WINDOW)
            val similarList = getSimilarMovieListUseCase(movieId ?: trendingList.results[0].id)
            _homeLiveData.postValue(HomeState.Success(trendingList, similarList))
        }catch (e: Exception){
            e.printStackTrace()
            _homeLiveData.postValue(HomeState.Error)
        }
    }

    companion object{
        const val MEDIA_TYPE = "movie"
        const val TIME_WINDOW = "day"
    }
}