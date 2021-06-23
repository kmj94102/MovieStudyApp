package com.example.moviestudyapp.network

import com.example.moviestudyapp.BuildConfig
import com.example.moviestudyapp.Constants
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("movie/{movie_id}")
    suspend fun getMovie(
        @Path("movie_id") id : Int,
        @Query("api_key") key: String,
        @Query("page") page : Int,
        @Query("language") lan : String
    ): Call<Movie>

    @GET("trending/{media_type}/{time_window}")
    suspend fun getTrendingMovieList(
        @Path("media_type") mediaType : String?,     // select => [all, movie, tv, person]
        @Path("time_window") timeWindow : String?,   // select => [day, week]
        @Query("api_key") apiKey : String?= BuildConfig.MOVIE_API_KEY
    ): TrendingList

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId : Long?,
        @Query("api_key") apiKey: String?= BuildConfig.MOVIE_API_KEY,
        @Query("language") language : String?= Constants.MOVIE_API_LANGUAGE
    ): MovieDetail

    @GET("movie/{movie_id}/credits")
    suspend fun getCredits(
        @Path("movie_id") movieId : Long?,
        @Query("api_key") apiKey: String?= BuildConfig.MOVIE_API_KEY,
        @Query("language") language : String?= Constants.MOVIE_API_LANGUAGE
    ): CreditsList
}