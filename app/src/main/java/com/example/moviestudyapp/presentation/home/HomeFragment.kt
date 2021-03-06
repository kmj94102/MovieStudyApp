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
    private lateinit var trendingAdapter : TrendingAdapter
    private lateinit var similarAdapter: SimilarAdapter

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        FragmentHomeBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun observeData() {
        viewModel.homeLiveData.observe(this) {
            when(it){
                is HomeState.UnInitialized -> {
                    Log.e("++++++", "UnInitialized")
                    // ?????? ?????? ?????????
                    initViews()
                }
                is HomeState.Loading -> {
                    Log.e("++++++", "Loading")
                    // ?????? ?????? ??????
                    handleLoadingState()
                }
                is HomeState.Success -> {
                    Log.e("++++++", "Success")
                    // ?????? ?????? ??????
                    handleSuccessState(it)
                }
                is HomeState.Error -> {
                    Log.e("++++++", "Error")
                    // ?????? ?????? ??????
                    handleErrorState()
                }
            }
        }
    }

    /**
     * ?????? ?????? ?????????
     * */
    private fun initViews() = with(binding){
        // ????????? ???????????? ??????
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

        // ????????? ???????????? ????????? ?????? ??? ??????
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

        // ???????????? ?????????????????? ??????
        similarAdapter = SimilarAdapter(requireActivity()){ movieId ->
            val intent = Intent(requireContext(), MovieDetailActivity::class.java)
            intent.putExtra(Constants.INTENT_MOVIE_ID, movieId)
            startActivity(intent)
        }

        rvRecommendMovie.adapter = similarAdapter

        // ???????????? ??????
        btnError.setOnClickListener {
            viewModel.fetchData()
        }
    }

    /**
     * ?????? ?????? ??????
     * */
    private fun handleLoadingState() = with(binding){
        progressBar.isVisible = true
    }

    /**
     * ?????? ?????? ??????
     * 1. adapter ??? initialized ?????? ?????? -> ??? ????????? ?????? initViews ??? ??? ??????
     * 2. ??????????????? ?????? ??? ??? ????????? ??????
     * 3. ??? adapter ????????? ?????? ??? ??????
     * */
    private fun handleSuccessState(homeState: HomeState.Success) = with(binding){
        if(::trendingAdapter.isInitialized.not() || ::similarAdapter.isInitialized.not()){
            initViews()
        }

        successViewVisible()

        trendingAdapter.addTrendingList(homeState.trendingList.results)

        similarAdapter.submitList(homeState.similarListResult.results)

    }

    /**
     * ?????? ?????? ??????
     * */
    private fun handleErrorState() = with(binding){
        errorViewVisible()
    }

    /**
     * ?????? ?????? ??? ????????? ??? ????????? ??????
     * */
    private fun successViewVisible() = with(binding){
        progressBar.isVisible = false
        txtTrending.isVisible = true
        vpTrending.isVisible = true
        txtMovieTitle.isVisible = true
        txtRating.isVisible = true
        ratingBar.isVisible = true
        txtRecommendMovie.isVisible = true
        rvRecommendMovie.isVisible = true
        scrollView.isVisible = true
        txtError.isVisible = false
        btnError.isVisible = false
    }

    /**
     * ?????? ?????? ??? ????????? ??? ????????? ??????
     * */
    private fun errorViewVisible() = with(binding){
        progressBar.isVisible = false
        txtTrending.isVisible = false
        vpTrending.isVisible = false
        txtMovieTitle.isVisible = false
        txtRating.isVisible = false
        ratingBar.isVisible = false
        txtRecommendMovie.isVisible = false
        rvRecommendMovie.isVisible = false
        scrollView.isVisible = false
        txtError.isVisible = true
        btnError.isVisible = true
    }

    /**
     * ??????????????? ??? job, coroutineContext ??????
     * */
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