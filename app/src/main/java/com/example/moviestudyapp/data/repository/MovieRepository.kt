package com.example.moviestudyapp.data.repository

import com.example.moviestudyapp.data.entity.MyKeywordEntity
import com.example.moviestudyapp.data.entity.MyMovie
import com.example.moviestudyapp.network.*

interface MovieRepository {

    suspend fun getTrendingMovieList(mediaType : String?, timeWindow : String?) : TrendingListResult

    suspend fun getSimilarMovieList(movieId: Long?) : SimilarListResult

    suspend fun getMovieDetail(movieId : Long?) : MovieDetail

    suspend fun getCredits(movieId : Long?) : CreditsList

    suspend fun getSearchMovies(query : String?) : MovieSearchResult

    suspend fun getSearchPerson(name : String?) : PersonSearchResult

    suspend fun insertKeyword(myKeywordEntity: MyKeywordEntity)

    suspend fun selectKeywordLists() : List<String>

    suspend fun deleteKeyword(keyword : String) : Int

    suspend fun insertMyMovie(myMovie: MyMovie)

    suspend fun updateMyMove(isBookMark: Boolean, isLike: Boolean, myVoteAverage : Float, memo : String, movieId : Long?)

    suspend fun selectMyMovie(movieId : Long?) : MyMovie?

    suspend fun selectBookMarkList(isBookMark : Boolean = true) : List<MyMovie>

    suspend fun selectLikeList(isLike : Boolean = true) : List<MyMovie>

}