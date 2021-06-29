package com.example.moviestudyapp.data.repository

import android.util.Log
import com.example.moviestudyapp.network.CreditsList
import com.example.moviestudyapp.network.MovieDetail
import com.example.moviestudyapp.network.MovieApi
import com.example.moviestudyapp.network.TrendingList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.RuntimeException

class MovieRepositoryImpl(
    private val movieApi: MovieApi,
    private val dispatcher: CoroutineDispatcher
) : MovieRepository{
    override suspend fun getTrendingMovieList(mediaType: String?, timeWindow: String?): TrendingList =
        movieApi.getTrendingMovieList(mediaType = mediaType, timeWindow = timeWindow)
            .body()
            ?: throw RuntimeException("getTrendingMovieList API 호출 오류")

    override suspend fun getMovieDetail(movieId: Long?): MovieDetail = withContext(dispatcher){
        movieApi.getMovieDetail(movieId = movieId)
            .body()
            ?: throw RuntimeException("getMovieDetail API 호출 오류")
    }

    override suspend fun getCredits(movieId: Long?): CreditsList {
        return CreditsList(null, listOf())
    }

}