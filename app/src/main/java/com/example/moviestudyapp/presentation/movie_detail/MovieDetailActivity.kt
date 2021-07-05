package com.example.moviestudyapp.presentation.movie_detail

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.moviestudyapp.Constants
import com.example.moviestudyapp.R
import com.example.moviestudyapp.databinding.ActivityMovieDetailBinding
import com.example.moviestudyapp.presentation.BaseActivity
import com.example.moviestudyapp.presentation.adapter.CastInfoAdapter
import com.example.moviestudyapp.presentation.adapter.GenreAdapter
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.backgroundResource
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.coroutines.CoroutineContext

internal class MovieDetailActivity : BaseActivity<MovieDetailViewModel>(), CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override val viewModel: MovieDetailViewModel by viewModel{
        parametersOf(
            intent.getLongExtra(Constants.INTENT_MOVIE_ID, 0L)
        )
    }

    private val pref: SharedPreferences by lazy {
        getSharedPreferences(Constants.PREF_USER, Context.MODE_PRIVATE)
    }

    private val binding : ActivityMovieDetailBinding by lazy { ActivityMovieDetailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        coroutineContext.cancel()
    }

    override fun observeData() {
        viewModel.movieDetailLiveData.observe(this){
            when(it){
                is MovieDetailState.UnInitialized -> {
                    initViews()
                }
                is MovieDetailState.Loading -> {
                    handleLoadingState()
                }
                is MovieDetailState.Success -> {
                    handleSuccessState(it)
                }
                is MovieDetailState.Error -> {
                    handleErrorState()
                }
            }
        }
    }

    private fun initViews() = with(binding){
        pref.edit().putLong(Constants.PREF_KEY_MOVIE_ID, intent.getLongExtra(Constants.INTENT_MOVIE_ID, 0L)).apply()
    }

    private fun handleLoadingState() = with(binding){
        progressBar.isVisible = true
    }

    private fun handleSuccessState(movieDetailState: MovieDetailState.Success) = with(binding){
        progressBar.isVisible = false
        successVisibleViews()

        txtVoteAverage.text = "${movieDetailState.movieDetail.vote_average}"
        txtTitle.text = movieDetailState.movieDetail.title
        txtStory.text = movieDetailState.movieDetail.overview
        txtTitle.isSelected = true
        Glide
            .with(this@MovieDetailActivity)
            .load("${Constants.MOVIE_API_START_IMAGE_URL}${movieDetailState.movieDetail.backdrop_path}")
            .centerCrop()
            .into(imgBackgtround)

        val genreList = mutableListOf<String>()
        genreList.addAll(
            movieDetailState.movieDetail.genres.mapNotNull {
                    genre -> genre.name
            }
        )

        rvGenre.adapter = GenreAdapter(genreList)

        rvCast.adapter = CastInfoAdapter(this@MovieDetailActivity, movieDetailState.creditsList.cast)
    }

    private fun handleErrorState() = with(binding){
        progressBar.isVisible = false
        errorVisibleViews()
    }

    private fun successVisibleViews() = with(binding){
        viewContent.backgroundResource = R.drawable.bg_gradient2
        txtTitle.isVisible = true
        rvGenre.isVisible = true
        txtVoteAverage.isVisible = true
        viewHeart.isVisible = true
        viewBookMark.isVisible = true
        scrollView.isVisible = true
        txtError.isVisible = false
        btnError.isVisible = false
    }

    private fun errorVisibleViews() = with(binding){
        viewContent.backgroundColorResource = R.color.main_color
        txtTitle.isVisible = false
        rvGenre.isVisible = false
        txtVoteAverage.isVisible = false
        viewHeart.isVisible = false
        viewBookMark.isVisible = false
        scrollView.isVisible = false
        txtError.isVisible = true
        btnError.isVisible = true
    }

}