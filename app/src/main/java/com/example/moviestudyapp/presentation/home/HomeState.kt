package com.example.moviestudyapp.presentation.home

import com.example.moviestudyapp.network.SimilarListResult
import com.example.moviestudyapp.network.TrendingListResult

sealed class HomeState {

    object UnInitialized : HomeState()

    object Loading: HomeState()

    data class Success(
        val trendingList: TrendingListResult,
        val similarListResult: SimilarListResult
    ) : HomeState()

    object Error : HomeState()

}