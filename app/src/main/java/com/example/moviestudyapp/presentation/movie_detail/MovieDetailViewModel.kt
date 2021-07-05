package com.example.moviestudyapp.presentation.movie_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moviestudyapp.domain.GetCreditsUseCase
import com.example.moviestudyapp.domain.GetMovieDetailUseCase
import com.example.moviestudyapp.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

internal class MovieDetailViewModel(
    var movieId : Long?,
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getCreditsUseCase: GetCreditsUseCase
) : BaseViewModel() {

    private var _movieDetailLiveData = MutableLiveData<MovieDetailState>(MovieDetailState.UnInitialized)
    var movieDetailLiveData : LiveData<MovieDetailState> = _movieDetailLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        _movieDetailLiveData.postValue(MovieDetailState.Loading)
        try {
            val movieDetail = getMovieDetailUseCase(movieId)
            val creditsList = getCreditsUseCase(movieId)
            _movieDetailLiveData.postValue(MovieDetailState.Success(movieDetail, creditsList))
        }catch (e: Exception){
            e.printStackTrace()
            _movieDetailLiveData.postValue(MovieDetailState.Error)
        }
    }
}