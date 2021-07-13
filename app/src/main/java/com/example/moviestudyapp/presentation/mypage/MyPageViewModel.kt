package com.example.moviestudyapp.presentation.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moviestudyapp.domain.SelectBookMarkListUseCase
import com.example.moviestudyapp.domain.SelectLikeListUseCase
import com.example.moviestudyapp.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

internal class MyPageViewModel(
    private val selectBookMarkListUseCase: SelectBookMarkListUseCase,
    private val selectLikeListUseCase: SelectLikeListUseCase
) : BaseViewModel() {

    private var _myPageLiveData = MutableLiveData<MyPageState>(MyPageState.UnInitialized)
    val myPageLiveData : LiveData<MyPageState> = _myPageLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        _myPageLiveData.postValue(MyPageState.Loading)
        try {
            val bookMarkList = selectBookMarkListUseCase()
            val likeList = selectLikeListUseCase()
            _myPageLiveData.postValue(MyPageState.Success(bookMarkList, likeList))
        }catch (e : Exception){
            e.printStackTrace()
            _myPageLiveData.postValue(MyPageState.Error)
        }
    }

}