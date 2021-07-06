package com.example.moviestudyapp.presentation.search

import android.view.View
import com.example.moviestudyapp.network.MovieSearchResult

sealed class SearchMovieState {

    object UnInitialized : SearchMovieState()

    object Loading: SearchMovieState()

    data class SelectKeywordSuccess(
        val keywordList : List<String>
    ) : SearchMovieState()

    data class Success(
        val movieSearchResult: MovieSearchResult
    ) : SearchMovieState()

    data class DeleteSuccess(
        val count : Int,
        val cipView : View
    ) : SearchMovieState()

    object Error : SearchMovieState()

}