package com.example.moviestudyapp.presentation.movie_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moviestudyapp.data.entity.MyMovie
import com.example.moviestudyapp.domain.*
import com.example.moviestudyapp.domain.GetCreditsUseCase
import com.example.moviestudyapp.domain.GetMovieDetailUseCase
import com.example.moviestudyapp.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

internal class MovieDetailViewModel(
    var movieId : Long?,
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getCreditsUseCase: GetCreditsUseCase,
    private val getSearchPersonUseCase: GetSearchPersonUseCase,
    private val insertMyMovieUseCase : InsertMyMovieUseCase,
    private val updateMyMovieUseCase : UpdateMyMovieUseCase,
    private val selectMyMovieUseCase : SelectMyMovieUseCase,
) : BaseViewModel() {

    private var _movieDetailLiveData = MutableLiveData<MovieDetailState>(MovieDetailState.UnInitialized)
    var movieDetailLiveData : LiveData<MovieDetailState> = _movieDetailLiveData

    /**
     * 영화 상세 조회, 출연 인물 조회, 내 영화 정보 조회
     * */
    override fun fetchData(): Job = viewModelScope.launch {
        _movieDetailLiveData.postValue(MovieDetailState.Loading)
        try {
            val movieDetail = getMovieDetailUseCase(movieId)
            val creditsList = getCreditsUseCase(movieId)
            val myMovie = selectMyMovieUseCase(movieId)
            _movieDetailLiveData.postValue(MovieDetailState.Success(movieId, movieDetail, creditsList, myMovie))
        }catch (e: Exception){
            e.printStackTrace()
            _movieDetailLiveData.postValue(MovieDetailState.Error)
        }
    }

    /**
     * 인물 상세 조회
     * */
    fun getSearchPersonInfo(name: String?, id : Long?) = viewModelScope.launch {
        try{
            val personInfo = getSearchPersonUseCase(name).results.find { it.id == id }
            personInfo?.let {
                _movieDetailLiveData.postValue(MovieDetailState.PersonSearchSuccess(it))
            } ?:_movieDetailLiveData.postValue(MovieDetailState.PersonSearchError)
        }catch (e : Exception){
            _movieDetailLiveData.postValue(MovieDetailState.PersonSearchError)
        }
    }

    /**
     * 내 영화 정보 추가 (신규)
     * */
    fun insertMyMovie(myMovie : MyMovie) = viewModelScope.launch{
        insertMyMovieUseCase(myMovie)
    }

    /**
     * 내 영화 정보 수정
     * */
    fun updateMyMovie(isBookMark: Boolean, isLike: Boolean, myVoteAverage : Float, memo : String, movieId : Long?) = viewModelScope.launch {
        updateMyMovieUseCase(isBookMark, isLike, myVoteAverage, memo, movieId)
    }
}