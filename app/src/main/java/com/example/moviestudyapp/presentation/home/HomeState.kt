package com.example.moviestudyapp.presentation.home

import com.example.moviestudyapp.network.TrendingList

sealed class HomeState {

    object UnInitialized : HomeState()

    object Loading: HomeState()

    data class Success(
        val trendingList: TrendingList
    ) : HomeState()

    object Error : HomeState()

}