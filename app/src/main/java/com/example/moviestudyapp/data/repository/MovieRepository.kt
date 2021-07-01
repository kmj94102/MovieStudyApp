package com.example.moviestudyapp.data.repository

import com.example.moviestudyapp.network.CreditsList
import com.example.moviestudyapp.network.MovieDetail
import com.example.moviestudyapp.network.SimilarListResult
import com.example.moviestudyapp.network.TrendingListResult

interface MovieRepository {

    suspend fun getTrendingMovieList(mediaType : String?, timeWindow : String?) : TrendingListResult

    suspend fun getSimilarMovieList(movieId: Long?) : SimilarListResult

    suspend fun getMovieDetail(movieId : Long?) : MovieDetail

    suspend fun getCredits(movieId : Long?) : CreditsList

}