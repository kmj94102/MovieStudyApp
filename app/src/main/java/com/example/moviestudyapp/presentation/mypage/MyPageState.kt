package com.example.moviestudyapp.presentation.mypage

import com.example.moviestudyapp.data.entity.MyMovie

sealed class MyPageState {

    object UnInitialized : MyPageState()

    object Loading : MyPageState()

    data class Success(
        val bookMarkList: List<MyMovie>,
        val likeList : List<MyMovie>
    ) : MyPageState()

    object Error : MyPageState()

}