package com.example.moviestudyapp.data.repository

import android.util.Log
import com.example.moviestudyapp.network.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.RuntimeException

class MovieRepositoryImpl(
    private val movieApi: MovieApi,
    private val dispatcher: CoroutineDispatcher
) : MovieRepository{
    override suspend fun getTrendingMovieList(mediaType: String?, timeWindow: String?): TrendingListResult =
        movieApi.getTrendingMovieList(mediaType = mediaType, timeWindow = timeWindow)
            .body()
            ?: throw RuntimeException("getTrendingMovieList API 호출 오류")

    override suspend fun getSimilarMovieList(movieId: Long?): SimilarListResult =
        movieApi.getSimilarMovieList(movieId = movieId)
            .body()
            ?: throw RuntimeException("getSimilarMovieList API 호출 오류")

    override suspend fun getMovieDetail(movieId: Long?): MovieDetail = withContext(dispatcher){
        movieApi.getMovieDetail(movieId = movieId)
            .body()
            ?: throw RuntimeException("getMovieDetail API 호출 오류")
    }

    override suspend fun getCredits(movieId: Long?): CreditsList = withContext(dispatcher){
        movieApi.getCredits(movieId = movieId)
            .body()
            ?: throw RuntimeException("getCredits API 호출 오류")
    }

}