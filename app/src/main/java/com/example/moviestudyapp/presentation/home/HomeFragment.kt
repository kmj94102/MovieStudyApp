package com.example.moviestudyapp.presentation.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.moviestudyapp.presentation.BaseFragment
import com.example.moviestudyapp.Constants
import com.example.moviestudyapp.presentation.movie_detail.MovieDetailActivity
import com.example.moviestudyapp.adapter.TrendingAdapter
import com.example.moviestudyapp.databinding.FragmentHomeBinding
import kotlinx.coroutines.*
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

        trendingAdapter = TrendingAdapter(requireActivity(), listOf()).apply {
            setListener { view ->
                val movieId = view.tag as Long?
                val intent = Intent(requireContext(), MovieDetailActivity::class.java)
                intent.putExtra(Constants.INTENT_MOVIE_ID, movieId)
                startActivity(intent)
            }
        }

        vpTrending.apply {
            adapter = trendingAdapter
            offscreenPageLimit = 3
            setPageTransformer(SliderTransformer(3))
        }
    }

    private fun handleLoadingState() = with(binding){
        progressBar.isVisible = true
    }

    private fun handleSuccessState(homeState: HomeState.Success) = with(binding){
        val convertList = homeState.trendingList.results.map {
            Pair("${Constants.MOVIE_API_START_IMAGE_URL}${it.backdrop_path}", it.id)
        }

        progressBar.isVisible = false
        trendingAdapter.addTrendingList(convertList)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        coroutineContext.cancel()
    }

}