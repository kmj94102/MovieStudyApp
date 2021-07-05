package com.example.moviestudyapp.presentation.search

import com.example.moviestudyapp.network.MovieSearchResult

sealed class SearchMovieState {

    object UnInitialized : SearchMovieState()

    object Loading: SearchMovieState()

    data class Success(
        val movieSearchResult : MovieSearchResult
    ) : SearchMovieState()

    object Error : SearchMovieState()

}