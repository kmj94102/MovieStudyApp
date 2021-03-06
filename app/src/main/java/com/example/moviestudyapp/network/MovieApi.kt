package com.example.moviestudyapp.network

import com.example.moviestudyapp.BuildConfig
import com.example.moviestudyapp.Constants
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/{movie_id}")
    suspend fun getMovie(
        @Path("movie_id") id : Int,
        @Query("api_key") key: String,
        @Query("page") page : Int,
        @Query("language") lan : String
    ): Call<Movie>

    // 인기 순위 영화 리스트 조회
    @GET("trending/{media_type}/{time_window}")
    suspend fun getTrendingMovieList(
        @Path("media_type") mediaType : String?,     // select => [all, movie, tv, person]
        @Path("time_window") timeWindow : String?,   // select => [day, week]
        @Query("api_key") apiKey : String?= BuildConfig.MOVIE_API_KEY,
        @Query("language") language : String?= Constants.MOVIE_API_LANGUAGE
    ): Response<TrendingListResult>

    // 비슷한 영화 리스트 조회
    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovieList(
        @Path("movie_id") movieId: Long?,
        @Query("api_key") apiKey: String?= BuildConfig.MOVIE_API_KEY,
        @Query("language") language : String?= Constants.MOVIE_API_LANGUAGE
    ): Response<SimilarListResult>

    // 영화 상세 조회
    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId : Long?,
        @Query("api_key") apiKey: String?= BuildConfig.MOVIE_API_KEY,
        @Query("language") language : String?= Constants.MOVIE_API_LANGUAGE
    ): Response<MovieDetail>

    // 출연진 조회
    @GET("movie/{movie_id}/credits")
    suspend fun getCredits(
        @Path("movie_id") movieId : Long?,
        @Query("api_key") apiKey: String?= BuildConfig.MOVIE_API_KEY,
        @Query("language") language : String?= Constants.MOVIE_API_LANGUAGE
    ): Response<CreditsList>

    // 영화 검색
    @GET("search/movie")
    suspend fun getSearchMovies(
        @Query("api_key") apiKey: String?= BuildConfig.MOVIE_API_KEY,
        @Query("language") language: String?= Constants.MOVIE_API_LANGUAGE,
        @Query("include_adult") includeAdult : Boolean? = true,
        @Query("page") page : Int? = 1,
        @Query("query") query : String?
    ) : Response<MovieSearchResult>

    // 인물 조회
    @GET("search/person")
    suspend fun getSearchPerson(
        @Query("api_key") apiKey: String?= BuildConfig.MOVIE_API_KEY,
        @Query("language") language: String?= Constants.MOVIE_API_LANGUAGE,
        @Query("page") page : Int?= 1,
        @Query("include_adult") include_adult : Boolean = true,
        @Query("query") name : String?
    ) : Response<PersonSearchResult>
}