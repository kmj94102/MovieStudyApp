package com.example.moviestudyapp.presentation.movie_detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.moviestudyapp.Constants
import com.example.moviestudyapp.R
import com.example.moviestudyapp.data.entity.MyMovie
import com.example.moviestudyapp.databinding.ActivityMovieDetailBinding
import com.example.moviestudyapp.network.MovieDetail
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
    private var movieId : Long? = 0
    private var movieDetail : MovieDetail?= null
    private var myMovie : MyMovie? = null


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

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initViews() = with(binding){
        pref.edit().putLong(Constants.PREF_KEY_MOVIE_ID, intent.getLongExtra(Constants.INTENT_MOVIE_ID, 0L)).apply()

        viewBookMark.setOnClickListener {
            it.tag = if(it.tag.toString() == "true") "false" else "true"
            setViewBookMarkBackground()

            myMovie?.let { myMovie ->
                myMovie.isBookMark = it.tag.toString() == "true"
                viewModel.updateMyMovie(myMovie.isBookMark, myMovie.isLike, movieId)
            } ?: run {
                myMovie = createMyMovie()
                viewModel.insertMyMovie(myMovie!!)
            }
        }

        viewHeart.setOnClickListener {
            Log.e("++++", "tag : ${it.tag}")
            it.tag = if(it.tag.toString() == "true") "false" else "true"
            Log.e("++++", "tag2 : ${it.tag}")
            setViewHeartBackground()

            myMovie?.let { myMovie ->
                myMovie.isLike = it.tag.toString() == "true"
                viewModel.updateMyMovie(myMovie.isBookMark, myMovie.isLike, movieId)
            } ?: run {
                myMovie = createMyMovie()
                viewModel.insertMyMovie(myMovie!!)
            }
        }
    }

    private fun createMyMovie() : MyMovie =
        MyMovie(
            movieId = movieId,
            isLike = binding.viewHeart.tag.toString().toBoolean(),
            isBookMark = binding.viewBookMark.tag.toString().toBoolean(),
            myVoteAverage = 0.0f,
            memo = "",
            title = movieDetail?.title,
            backdropPath = movieDetail?.backdrop_path,
            posterPath = movieDetail?.poster_path,
            genres = movieDetail?.genres?.map { it.name }?.joinToString("|"),
            releaseDate = movieDetail?.release_date,
            runtime = movieDetail?.runtime,
            voteAverage = movieDetail?.vote_average
        )

    private fun handleLoadingState() = with(binding){
        progressBar.isVisible = true
    }

    private fun handleSuccessState(movieDetailState: MovieDetailState.Success) = with(binding){
        progressBar.isVisible = false
        successVisibleViews()

        movieId = movieDetailState.movieId
        movieDetail = movieDetailState.movieDetail
        myMovie = movieDetailState.myMovie


        txtVoteAverage.text = "${movieDetailState.movieDetail.vote_average}"
        txtTitle.text = movieDetailState.movieDetail.title
        txtStory.text = movieDetailState.movieDetail.overview
        txtTitle.isSelected = true
        Glide
            .with(this@MovieDetailActivity)
            .load("${Constants.MOVIE_API_START_IMAGE_URL}${movieDetailState.movieDetail.backdrop_path}")
            .placeholder(R.color.main_color)
            .centerCrop()
            .into(imgBackgtround)

        rvGenre.adapter = GenreAdapter(movieDetailState.movieDetail.genres.mapNotNull { genre -> genre.name })
        rvCast.adapter = CastInfoAdapter(this@MovieDetailActivity, movieDetailState.creditsList.cast)

        viewBookMark.tag = if(movieDetailState.myMovie?.isBookMark == true) "true" else "false"
        viewHeart.tag = if(movieDetailState.myMovie?.isLike == true) "true" else "false"
        setViewBookMarkBackground()
        setViewHeartBackground()
    }

    private fun setViewBookMarkBackground() {
        binding.viewBookMark.backgroundResource = if(binding.viewBookMark.tag.toString() == "true") R.drawable.ic_bookmark_fill_24 else R.drawable.ic_bookmark_24
    }
    private fun setViewHeartBackground() {
        binding.viewHeart.backgroundResource = if(binding.viewHeart.tag.toString() == "true") R.drawable.ic_heart_fill_24 else R.drawable.ic_heart_24
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