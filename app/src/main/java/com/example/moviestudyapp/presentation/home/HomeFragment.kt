package com.example.moviestudyapp.presentation.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.example.moviestudyapp.Constants
import com.example.moviestudyapp.presentation.adapter.TrendingAdapter
import com.example.moviestudyapp.databinding.FragmentHomeBinding
import com.example.moviestudyapp.presentation.BaseFragment
import com.example.moviestudyapp.presentation.adapter.SimilarAdapter
import com.example.moviestudyapp.presentation.movie_detail.MovieDetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.coroutines.CoroutineContext

internal class HomeFragment : BaseFragment<HomeViewModel>(), CoroutineScope {
    private lateinit var binding : FragmentHomeBinding

    private val pref : SharedPreferences by lazy {
        requireActivity().getSharedPreferences(Constants.PREF_USER, Context.MODE_PRIVATE)
    }

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override val viewModel: HomeViewModel by viewModel{
        parametersOf(
            pref.getLong(Constants.PREF_KEY_MOVIE_ID, 0L)
        )
    }

    private lateinit var trendingAdapter : TrendingAdapter
    private lateinit var similarAdapter: SimilarAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        FragmentHomeBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun observeData() {
        viewModel.homeLiveData.observe(this) {
            when(it){
                is HomeState.UnInitialized -> {
                    Log.e("++++++", "UnInitialized")
                    // 각종 뷰들 초기화
                    initViews()
                }
                is HomeState.Loading -> {
                    Log.e("++++++", "Loading")
                    // 로딩 상태 설정
                    handleLoadingState()
                }
                is HomeState.Success -> {
                    Log.e("++++++", "Success")
                    // 완료 상태 설정
                    handleSuccessState(it)
                }
                is HomeState.Error -> {
                    Log.e("++++++", "Error")
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
        // 트래드 뷰페이저 설정
        trendingAdapter = TrendingAdapter(requireActivity(), listOf()){ movieId ->
            val intent = Intent(requireContext(), MovieDetailActivity::class.java)
            intent.putExtra(Constants.INTENT_MOVIE_ID, movieId)
            startActivity(intent)
        }

        vpTrending.apply {
            adapter = trendingAdapter
            offscreenPageLimit = 3
            setPageTransformer(SliderTransformer(3))
        }

        // 트렌드 뷰페이저 페이지 변경 시 셋팅
        vpTrending.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                Handler(Looper.getMainLooper()).postDelayed({
                    val trendingItem = trendingAdapter.currentItem(position)
                    ratingBar.rating = (trendingItem.vote_average ?: 0.0f) / 2f
                    txtRating.text = "${trendingItem.vote_average ?: 0.0f}"
                    txtMovieTitle.text = "${trendingItem.title}"
                }, 100)

            }
        })

        // 영화추천 리사이클러뷰 설정
        similarAdapter = SimilarAdapter(requireActivity()){ movieId ->
            val intent = Intent(requireContext(), MovieDetailActivity::class.java)
            intent.putExtra(Constants.INTENT_MOVIE_ID, movieId)
            startActivity(intent)
        }

        rvRecommendMovie.adapter = similarAdapter
    }

    /**
     * 로딩 상태 설정
     * */
    private fun handleLoadingState() = with(binding){
        progressBar.isVisible = true
    }

    /**
     * 완료 상태 설정
     * 1. adapter 들 initialized 상태 확인 -> 안 되었을 경우 initViews 로 선 실행
     * 2. 프로그레스 제거 및 뷰 활성화 설정
     * 3. 각 adapter 조회한 결과 값 추가
     * */
    private fun handleSuccessState(homeState: HomeState.Success) = with(binding){
        if(::trendingAdapter.isInitialized.not() || ::similarAdapter.isInitialized.not()){
            initViews()
        }

        progressBar.isVisible = false
        successViewVisible()

        trendingAdapter.addTrendingList(homeState.trendingList.results)

        similarAdapter.submitList(homeState.similarListResult.results)

    }

    /**
     * 에러 상태 설정
     * */
    private fun handleErrorState() = with(binding){
        progressBar.isVisible = false
        errorViewVisible()
    }

    /**
     * 완료 상태 시 보여줄 뷰 활성화 설정
     * */
    private fun successViewVisible() = with(binding){
        txtTrending.isVisible = true
        vpTrending.isVisible = true
        txtMovieTitle.isVisible = true
        txtRating.isVisible = true
        ratingBar.isVisible = true
        txtRecommendMovie.isVisible = true
        rvRecommendMovie.isVisible = true
        txtError.isVisible = false
        btnError.isVisible = false
    }

    /**
     * 에러 상태 시 보여줄 뷰 활성화 설정
     * */
    private fun errorViewVisible() = with(binding){
        txtTrending.isVisible = false
        vpTrending.isVisible = false
        txtMovieTitle.isVisible = false
        txtRating.isVisible = false
        ratingBar.isVisible = false
        txtRecommendMovie.isVisible = false
        rvRecommendMovie.isVisible = false
        txtError.isVisible = true
        btnError.isVisible = true
    }

    /**
     * 종료되었을 때 job, coroutineContext 취소
     * */
    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        coroutineContext.cancel()
    }

}