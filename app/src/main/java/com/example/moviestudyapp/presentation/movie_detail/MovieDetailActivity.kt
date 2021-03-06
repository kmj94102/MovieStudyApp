package com.example.moviestudyapp.presentation.movie_detail

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.edit
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
import com.example.moviestudyapp.presentation.dialog.LikeMovieDialog
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.backgroundResource
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.coroutines.CoroutineContext


internal class MovieDetailActivity : BaseActivity<MovieDetailViewModel>(), CoroutineScope {

    private val binding : ActivityMovieDetailBinding by lazy { ActivityMovieDetailBinding.inflate(layoutInflater) }

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
    private var movieId : Long? = 0
    private var movieDetail : MovieDetail?= null
    private var myMovie : MyMovie? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun observeData() {
        viewModel.movieDetailLiveData.observe(this){
            when(it){
                is MovieDetailState.UnInitialized -> {
                    // ?????? ?????? ?????????
                    initViews()
                }
                is MovieDetailState.Loading -> {
                    // ?????? ?????? ??????
                    handleLoadingState()
                }
                is MovieDetailState.Success -> {
                    // ?????? ?????? ??????
                    handleSuccessState(it)
                }
                is MovieDetailState.Error -> {
                    // ?????? ?????? ??????
                    handleErrorState()
                }
                is MovieDetailState.PersonSearchSuccess -> {
                    // ?????? ?????? ?????? ?????? ?????? ??????
                    handlePersonSearchSuccess(it)
                }
                is MovieDetailState.PersonSearchError -> {
                    // ?????? ?????? ?????? ?????? ?????? ??????
                    handlePersonSearchError()
                }
            }
        }
    }

    /**
     * ?????? ?????? ????????? : ????????? ??? ????????? ?????? ??????
     * */
    private fun initViews(){}


    /**
     * ????????? ?????? ?????? ?????????
     * */
    fun setBookMarkClickListener(view : View){
        view.tag = if(view.tag.toString() == "true") "false" else "true"
        setViewBookMarkBackground()

        myMovie?.let { myMovie ->
            // Database ?????? ?????? ??? ????????????
            myMovie.isBookMark = view.tag.toString() == "true"
            viewModel.updateMyMovie(myMovie.isBookMark, myMovie.isLike, myMovie.myVoteAverage?: 0.0f, myMovie.memo ?: "", movieId)
        } ?: run {
            // Database ?????? ?????? ??? ?????? ??????
            myMovie = createMyMovie(0.0f, "")
            viewModel.insertMyMovie(myMovie!!)
        }
    }


    /**
     * ????????? ?????? ?????? ?????????
     * */
    fun setHeartClickListener(view: View){
        view.tag = if(view.tag.toString() == "true") "false" else "true"

        myMovie?.isLike = view.tag.toString() == "true"

        if(view.tag.toString() == "true"){
            // ??? ??????, ?????? ??????????????? ??????
            LikeMovieDialog(this@MovieDetailActivity){ myVoteAverage, memo ->
                myMovie?.let { myMovie ->
                    // Database ?????? ?????? ??? ????????????
                    myMovie.isLike = view.tag.toString() == "true"
                    myMovie.myVoteAverage = myVoteAverage
                    myMovie.memo = memo
                    viewModel.updateMyMovie(myMovie.isBookMark, myMovie.isLike, myVoteAverage, memo, movieId)
                } ?: run {
                    // Database ?????? ?????? ??? ?????? ??????
                    myMovie = createMyMovie(myVoteAverage, memo)
                    viewModel.insertMyMovie(myMovie!!)
                }
                setViewHeartBackground()
            }.show()
        }else{
            // Database ?????? ?????? ??? ????????????
            myMovie?.let { myMovie ->
                viewModel.updateMyMovie(myMovie.isBookMark, myMovie.isLike, myMovie.myVoteAverage?: 0.0f, myMovie.memo ?: "", movieId)
            }
            setViewHeartBackground()
        }
    }


    /**
     * ????????? ?????? ?????? ?????? ?????? ?????? ??????
     * */
    private fun setViewBookMarkBackground() {
        binding.viewBookMark.backgroundResource = if(binding.viewBookMark.tag.toString() == "true") R.drawable.ic_bookmark_fill_24 else R.drawable.ic_bookmark_24
    }


    /**
     * ????????? ?????? ?????? ?????? ?????? ?????? ??????
     * */
    private fun setViewHeartBackground() {
        binding.viewHeart.backgroundResource = if(binding.viewHeart.tag.toString() == "true") R.drawable.ic_heart_fill_24 else R.drawable.ic_heart_24
    }


    /**
     * Database ?????? ?????? ??? ?????? ??????
     * */
    private fun createMyMovie(myVoteAverage : Float, memo : String) : MyMovie =
        MyMovie(
            movieId = movieId,
            isLike = binding.viewHeart.tag.toString().toBoolean(),
            isBookMark = binding.viewBookMark.tag.toString().toBoolean(),
            myVoteAverage = myVoteAverage,
            memo = memo,
            title = movieDetail?.title,
            backdropPath = movieDetail?.backdrop_path,
            posterPath = movieDetail?.poster_path,
            genres = movieDetail?.genres?.map { it.name }?.joinToString("|"),
            releaseDate = movieDetail?.release_date,
            runtime = movieDetail?.runtime,
            voteAverage = movieDetail?.vote_average
        )


    /**
     * ?????? ?????? ??????
     * */
    private fun handleLoadingState() = with(binding){
        progressBar.isVisible = true
    }


    /**
     * ?????? ?????? ??????
     * */
    private fun handleSuccessState(movieDetailState: MovieDetailState.Success) = with(binding){
        progressBar.isVisible = false
        // ?????? ?????? ??? ????????? ??? ????????? ??????
        successVisibleViews()

        movieId = movieDetailState.movieId
        movieDetail = movieDetailState.movieDetail
        myMovie = movieDetailState.myMovie

        // ????????? ?????? ?????? ?????? (??? ???????????? ?????? ?????? ?????? ??? ??????)
        pref.edit{
            putLong(Constants.PREF_KEY_MOVIE_ID, movieId ?: 0)
        }

        // ?????? ?????? ??? ??????
        bindViews(movieDetailState)

    }


    /**
     * ?????? ?????? ??? ??????
     * */
    private fun bindViews(movieDetailState: MovieDetailState.Success) = with(binding){
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
        rvCast.adapter = CastInfoAdapter{ name, id ->
            // ?????? ?????? ??????
            viewModel.getSearchPersonInfo(name, id)
        }
        (rvCast.adapter as? CastInfoAdapter)?.submitList(movieDetailState.creditsList.cast)

        viewBookMark.tag = if(movieDetailState.myMovie?.isBookMark == true) "true" else "false"
        viewHeart.tag = if(movieDetailState.myMovie?.isLike == true) "true" else "false"
        setViewBookMarkBackground()
        setViewHeartBackground()
    }


    /**
     * ?????? ?????? ??????
     * */
    private fun handleErrorState() = with(binding){
        progressBar.isVisible = false
        errorVisibleViews()
    }


    /**
     * ?????? ?????? ??? ????????? ??? ????????? ??????
     * */
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


    /**
     * ?????? ?????? ??? ????????? ??? ????????? ??????
     * */
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


    /**
     * ?????? ?????? ?????? ?????? ?????? ??????
     * todo ?????? ?????? ??????
     * */
    private fun handlePersonSearchSuccess(movieDetailState: MovieDetailState.PersonSearchSuccess) {
        val personInfo = movieDetailState.personInfo
        Log.e("+++++", "info : ${personInfo.name} / ${personInfo.known_for[0].title}")
    }


    /**
     * ?????? ?????? ?????? ?????? ?????? ??????
     * todo ?????? ?????? ??????
     * */
    private fun handlePersonSearchError(){

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