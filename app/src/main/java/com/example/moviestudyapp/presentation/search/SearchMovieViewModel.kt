package com.example.moviestudyapp.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moviestudyapp.domain.GetSearchMoviesUseCase
import com.example.moviestudyapp.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

internal class SearchMovieViewModel(
    private val getSearchMoviesUseCase: GetSearchMoviesUseCase
) : BaseViewModel(){

    private var _searchMovieLiveData = MutableLiveData<SearchMovieState>(SearchMovieState.UnInitialized)
    val searchMovieLiveData : LiveData<SearchMovieState> = _searchMovieLiveData

    override fun fetchData(): Job = viewModelScope.launch{

    }

    fun searchMove(query : String?) = viewModelScope.launch{
        _searchMovieLiveData.postValue(SearchMovieState.Loading)
        try {
            _searchMovieLiveData.postValue(SearchMovieState.Success(getSearchMoviesUseCase(query)))
        }catch (e: Exception){
            e.printStackTrace()
            _searchMovieLiveData.postValue(SearchMovieState.Error)
        }
    }
}