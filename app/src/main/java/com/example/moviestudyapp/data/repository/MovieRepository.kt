package com.example.moviestudyapp.data.repository

import com.example.moviestudyapp.network.CreditsList
import com.example.moviestudyapp.network.MovieDetail
import com.example.moviestudyapp.network.TrendingList

interface MovieRepository {

    suspend fun getTrendingMovieList(mediaType : String?, timeWindow : String?) : TrendingList

    suspend fun getMovieDetail(movieId : Long?) : MovieDetail

    suspend fun getCredits(movieId : Long?) : CreditsList

}