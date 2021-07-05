package com.example.moviestudyapp.presentation.search

import com.example.moviestudyapp.network.MovieSearchResult

sealed class SearchMovieState {

    object UnInitialized : SearchMovieState()

    data class InitializedSuccess(
        val keywordList : List<String>
    ) : SearchMovieState()

    object Loading: SearchMovieState()

    data class Success(
        val movieSearchResult : MovieSearchResult
    ) : SearchMovieState()

    object Error : SearchMovieState()

}