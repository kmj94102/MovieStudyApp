package com.example.moviestudyapp.presentation.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
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
import kotlinx.coroutines.cancel
import org.jetbrains.anko.startActivity
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext
import kotlin.math.roundToInt

internal class MyPageFragment : BaseFragment<MyPageViewModel>(), CoroutineScope {

    private lateinit var binding : FragmentMyPageBinding
    private lateinit var likeMovieAdapter : LikeMovieAdapter
    private lateinit var bookmarkAdapter: BookmarkAdapter

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override val viewModel: MyPageViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        FragmentMyPageBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun observeData() {
        viewModel.myPageLiveData.observe(this){
            when(it){
                is MyPageState.UnInitialized -> {
                    // 각종 뷰들 초기화
                    initViews()
                }
                is MyPageState.Loading -> {
                    // 로딩 상태 설정
                    handleLoadingState()
                }
                is MyPageState.Success -> {
                    // 완료 상태 설정
                    handleSuccessState(it)
                }
                is MyPageState.Error -> {
                    // 에러 상태 설정
                    handleErrorState()
                }
            }
        }
    }

    /**
     * 각종 뷰들 초기화
     * */
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

        // 새로고침 버튼
        btnError.setOnClickListener {
            viewModel.fetchData()
        }

    }

    /**
     * 로딩 상태 설정
     * */
    private fun handleLoadingState() = with(binding){
        progressBar.isVisible = true
    }

    /**
     * 완료 상태 설정
     * */
    private fun handleSuccessState(myPageState: MyPageState.Success) = with(binding){
        successViewsVisible(myPageState)

        if(this@MyPageFragment::likeMovieAdapter.isInitialized.not()){
            initViews()
        }

        likeMovieAdapter.submitList(myPageState.likeList)
        bookmarkAdapter.submitList(myPageState.bookMarkList)

    }

    /**
     * 에러 상태 설정
     * */
    private fun handleErrorState() {
        errorViewsVisible()
    }

    /**
     * 완료 상태 시 보여줄 뷰 활성화 설정
     * */
    private fun successViewsVisible(myPageState: MyPageState.Success) = with(binding){
        progressBar.isVisible = false
        scrollView.isVisible = true
        btnError.isVisible = false
        txtError.isVisible = false

        // 조회 결과가 없을 때에는 TextView 띄우기
        rvBookmark.isVisible = myPageState.bookMarkList.isNotEmpty()
        txtEmptyBookmark.isVisible = myPageState.bookMarkList.isEmpty()
        rvLikeMovie.isVisible = myPageState.likeList.isNotEmpty()
        txtEmptyLikeMovie.isVisible = myPageState.likeList.isEmpty()

        // 좋아요 조회 결과가 없을 때 UI 수정
        val layoutParams = txtBookmark.layoutParams as ConstraintLayout.LayoutParams
        val dp = requireActivity().resources.displayMetrics
        layoutParams.topToBottom = if (myPageState.likeList.isEmpty()) txtEmptyLikeMovie.id else rvLikeMovie.id
        layoutParams.topMargin = if (myPageState.likeList.isEmpty()) (40 * dp.density).roundToInt() else (20 * dp.density).roundToInt()

        txtBookmark.layoutParams = layoutParams

    }

    /**
     * 에러 상태 시 보여줄 뷰 활성화 설정
     * */
    private fun errorViewsVisible() = with(binding){
        progressBar.isVisible = false
        scrollView.isVisible = false
        btnError.isVisible = true
        txtError.isVisible = true
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        coroutineContext.cancel()
    }

    override fun onResume() {
        super.onResume()
        initViews()
        viewModel.fetchData()
    }

}