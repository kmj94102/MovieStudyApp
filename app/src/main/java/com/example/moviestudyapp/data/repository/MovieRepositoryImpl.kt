package com.example.moviestudyapp.data.repository

import android.util.Log
import com.example.moviestudyapp.data.dao.MovieDao
import com.example.moviestudyapp.data.entity.MyKeywordEntity
import com.example.moviestudyapp.data.entity.MyMovie
import com.example.moviestudyapp.network.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.RuntimeException

class MovieRepositoryImpl(
    private val movieApi: MovieApi,
    private val movieDao: MovieDao,
    private val dispatcher: CoroutineDispatcher
) : MovieRepository{

    /**
     * Movie Api 관련
     * */
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

    override suspend fun getSearchMovies(query: String?): MovieSearchResult = withContext(dispatcher) {
        movieApi.getSearchMovies(query = query)
            .body()
            ?: throw RuntimeException("getSearchMovies API 호출 오류")
    }

    /**
     * 키워드 관련
     * */
    override suspend fun insertKeyword(myKeywordEntity: MyKeywordEntity) = withContext(dispatcher) {
        movieDao.insertKeyword(myKeywordEntity)
    }

    override suspend fun selectKeywordLists(): List<String> = withContext(dispatcher) {
        movieDao.selectKeywordLists()
    }

    override suspend fun deleteKeyword(keyword: String): Int = withContext(dispatcher) {
        movieDao.deleteKeyword(keyword)
    }

    /**
     * 마이페이지 관련
     * */
    override suspend fun insertMyMovie(myMovie: MyMovie) = withContext(dispatcher) {
        movieDao.insertMyMovie(myMovie)
    }

    override suspend fun updateMyMove(isBookMark: Boolean, isLike: Boolean, movieId : Long?) = withContext(dispatcher) {
        movieDao.updateMyMove(isBookMark, isLike, movieId)
    }

    override suspend fun selectMyMovie(movieId: Long?): MyMovie? = withContext(dispatcher) {
        movieDao.selectMyMovie(movieId)
    }

    override suspend fun selectBookMarkList(isBookMark: Boolean): List<MyMovie> = withContext(dispatcher) {
        movieDao.selectBookMarkList()
    }

    override suspend fun selectLikeList(isLike: Boolean): List<MyMovie> = withContext(dispatcher) {
        movieDao.selectLikeList()
    }

}