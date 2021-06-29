package com.example.moviestudyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.moviestudyapp.adapter.CastInfoAdapter
import com.example.moviestudyapp.adapter.GenreAdapter
import com.example.moviestudyapp.databinding.ActivityMovieDetailBinding
import com.example.moviestudyapp.network.CreditsList
import com.example.moviestudyapp.network.MovieDetail
import com.example.moviestudyapp.network.RetrofitUtil
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.jetbrains.anko.toast
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class MovieDetailActivity : AppCompatActivity(), CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val binding : ActivityMovieDetailBinding by lazy { ActivityMovieDetailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val movieId = intent.getLongExtra(Constants.INTENT_MOVIE_ID, 0)

        if(movieId <= 0){
            toast("movieID 값에 문제 발생하였습니다.")
            finish()
        }

        initialize(movieId)
    }

    private fun initialize(movieId : Long) = launch(coroutineContext) {
        try {
            val movieDetail = getMovieDetail(movieId)
            val creditsList = getCredits(movieId)

            bindGenreAdapter(movieDetail)
            bindMovieDetail(movieDetail)
            bindCreditInfoAdapter(creditsList)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private suspend fun getMovieDetail(movieId : Long) = withContext(Dispatchers.IO){
        RetrofitUtil.movieService.getMovieDetail(movieId = movieId)
    }

    private suspend fun getCredits(movieId : Long)= withContext(Dispatchers.IO){
        RetrofitUtil.movieService.getCredits(movieId)
    }

    private suspend fun bindGenreAdapter(movieDetail: MovieDetail) = withContext(coroutineContext) {
        val genreList = mutableListOf<String>()

        genreList.addAll(
            movieDetail.genres.mapNotNull {
                    genre -> genre.name
            }
        )

        binding.rvGenre.adapter = GenreAdapter(genreList)
    }

    private suspend fun bindMovieDetail(movieDetail: MovieDetail) = withContext(coroutineContext){
        binding.txtVoteAverage.text = "${movieDetail.vote_average}"
        binding.txtTitle.text = movieDetail.title
        binding.txtStory.text = movieDetail.overview
        binding.txtTitle.isSelected = true
        Glide
            .with(this@MovieDetailActivity)
            .load("${Constants.MOVIE_API_START_IMAGE_URL}${movieDetail.backdrop_path}")
            .centerCrop()
            .into(binding.imgBackgtround)
    }

    private suspend fun bindCreditInfoAdapter(creditsList: CreditsList) = withContext(coroutineContext){
        binding.rvCast.adapter = CastInfoAdapter(this@MovieDetailActivity, creditsList.cast)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        coroutineContext.cancel()
    }

}