package com.example.moviestudyapp.presentation.movie_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moviestudyapp.data.entity.MyMovie
import com.example.moviestudyapp.domain.*
import com.example.moviestudyapp.domain.GetCreditsUseCase
import com.example.moviestudyapp.domain.GetMovieDetailUseCase
import com.example.moviestudyapp.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

internal class MovieDetailViewModel(
    var movieId : Long?,
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getCreditsUseCase: GetCreditsUseCase,
    private val insertMyMovieUseCase : InsertMyMovieUseCase,
    private val updateMyMovieUseCase : UpdateMyMovieUseCase,
    private val selectMyMovieUseCase : SelectMyMovieUseCase
) : BaseViewModel() {

    private var _movieDetailLiveData = MutableLiveData<MovieDetailState>(MovieDetailState.UnInitialized)
    var movieDetailLiveData : LiveData<MovieDetailState> = _movieDetailLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        _movieDetailLiveData.postValue(MovieDetailState.Loading)
        try {
            val movieDetail = getMovieDetailUseCase(movieId)
            val creditsList = getCreditsUseCase(movieId)
            val myMovie = selectMyMovieUseCase(movieId)
            _movieDetailLiveData.postValue(MovieDetailState.Success(movieId, movieDetail, creditsList, myMovie))
        }catch (e: Exception){
            e.printStackTrace()
            _movieDetailLiveData.postValue(MovieDetailState.Error)
        }
    }

    fun insertMyMovie(myMovie : MyMovie) = viewModelScope.launch{
        insertMyMovieUseCase(myMovie)
    }

    fun updateMyMovie(isBookMark: Boolean, isLike: Boolean, myVoteAverage : Float, memo : String, movieId : Long?) = viewModelScope.launch {
        updateMyMovieUseCase(isBookMark, isLike, myVoteAverage, memo, movieId)
    }
}