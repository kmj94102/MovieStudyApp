package com.example.moviestudyapp.data.repository

import com.example.moviestudyapp.network.*

interface MovieRepository {

    suspend fun getTrendingMovieList(mediaType : String?, timeWindow : String?) : TrendingListResult

    suspend fun getSimilarMovieList(movieId: Long?) : SimilarListResult

    suspend fun getMovieDetail(movieId : Long?) : MovieDetail

    suspend fun getCredits(movieId : Long?) : CreditsList

    suspend fun getSearchMovies(query : String?) : MovieSearchResult

}