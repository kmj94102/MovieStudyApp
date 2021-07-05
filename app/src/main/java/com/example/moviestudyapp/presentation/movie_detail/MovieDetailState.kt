package com.example.moviestudyapp.presentation.movie_detail

import com.example.moviestudyapp.network.CreditsList
import com.example.moviestudyapp.network.MovieDetail

sealed class MovieDetailState {

    object UnInitialized : MovieDetailState()

    object Loading: MovieDetailState()

    data class Success(
        val movieDetail : MovieDetail,
        val creditsList: CreditsList
    ) : MovieDetailState()

    object Error : MovieDetailState()

}