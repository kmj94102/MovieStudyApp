package com.example.moviestudyapp.presentation.mypage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import com.example.moviestudyapp.Constants
import com.example.moviestudyapp.GridSpacingItemDecoration
import com.example.moviestudyapp.R
import com.example.moviestudyapp.databinding.FragmentMyPageBinding
import com.example.moviestudyapp.presentation.BaseFragment
import com.example.moviestudyapp.presentation.adapter.BookmarkAdapter
import com.example.moviestudyapp.presentation.adapter.LikeMovieAdapter
import com.example.moviestudyapp.presentation.movie_detail.MovieDetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.jetbrains.anko.startActivity
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext

internal class MyPageFragment : BaseFragment<MyPageViewModel>(), CoroutineScope {

    private lateinit var binding : FragmentMyPageBinding
    private lateinit var likeMovieAdapter : LikeMovieAdapter
    private lateinit var bookmarkAdapter: BookmarkAdapter

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override val viewModel: MyPageViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentMyPageBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun observeData() {
        viewModel.myPageLiveData.observe(this){
            when(it){
                is MyPageState.UnInitialized -> {
                    initViews()
                }
                is MyPageState.Loading -> {
                    handleLoadingState()
                }
                is MyPageState.Success -> {
                    handleSuccessState(it)
                }
                is MyPageState.Error -> {
                    handleErrorState()
                }
            }
        }
    }

    private fun initViews() = with(binding){
        likeMovieAdapter = LikeMovieAdapter {
            requireActivity().startActivity<MovieDetailActivity>(Constants.INTENT_MOVIE_ID to it)
        }
        rvLikeMovie.adapter = likeMovieAdapter

        bookmarkAdapter = BookmarkAdapter {
            requireActivity().startActivity<MovieDetailActivity>(Constants.INTENT_MOVIE_ID to it)
        }
        val spacing = resources.getDimensionPixelSize(R.dimen.spacing_10dp)
        rvBookmark.apply {
            adapter = bookmarkAdapter
            addItemDecoration(GridSpacingItemDecoration(spacing))
        }


    }

    private fun handleLoadingState() = with(binding){

    }

    private fun handleSuccessState(myPageState: MyPageState.Success) = with(binding){
        if(this@MyPageFragment::likeMovieAdapter.isInitialized.not()){
            initViews()
        }

        likeMovieAdapter.submitList(myPageState.likeList)
        bookmarkAdapter.submitList(myPageState.bookMarkList)

    }

    private fun handleErrorState() = with(binding){

    }
}