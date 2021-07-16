package com.example.moviestudyapp.presentation.movie_detail

import com.example.moviestudyapp.data.entity.MyMovie
import com.example.moviestudyapp.network.CreditsList
import com.example.moviestudyapp.network.MovieDetail
import com.example.moviestudyapp.network.PersonSearchList

sealed class MovieDetailState {

    object UnInitialized : MovieDetailState()

    object Loading: MovieDetailState()

    data class Success(
        val movieId : Long?,
        val movieDetail : MovieDetail,
        val creditsList: CreditsList,
        val myMovie : MyMovie?
    ) : MovieDetailState()

    data class PersonSearchSuccess(
        val personInfo : PersonSearchList
    ) : MovieDetailState()

    object PersonSearchError : MovieDetailState()

    object Error : MovieDetailState()

}