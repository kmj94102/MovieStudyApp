package com.example.moviestudyapp.presentation.home

import android.content.Intent
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
import com.example.moviestudyapp.adapter.TrendingAdapter
import com.example.moviestudyapp.databinding.FragmentHomeBinding
import com.example.moviestudyapp.presentation.BaseFragment
import com.example.moviestudyapp.presentation.movie_detail.MovieDetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext

internal class HomeFragment : BaseFragment<HomeViewModel>(), CoroutineScope {
    private lateinit var binding : FragmentHomeBinding

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override val viewModel: HomeViewModel by viewModel()

    private lateinit var trendingAdapter : TrendingAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        FragmentHomeBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun observeData() {
        viewModel.homeLiveData.observe(this) {
            when(it){
                is HomeState.UnInitialized -> {
                    Log.e("++++++", "UnInitialized")
                    initViews()
                }
                is HomeState.Loading -> {
                    Log.e("++++++", "Loading")
                    handleLoadingState()
                }
                is HomeState.Success -> {
                    Log.e("++++++", "Success")
                    handleSuccessState(it)
                }
                is HomeState.Error -> {

                }
            }
        }
    }

    private fun initViews() = with(binding){
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
    }

    private fun handleLoadingState() = with(binding){
        progressBar.isVisible = true
    }

    private fun handleSuccessState(homeState: HomeState.Success) = with(binding){
        if(::trendingAdapter.isInitialized.not()){
            initViews()
        }

        progressBar.isVisible = false
        trendingAdapter.addTrendingList(homeState.trendingList.results)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        coroutineContext.cancel()
    }

}