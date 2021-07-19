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
                    // 각종 뷰들 초기화
                    initViews()
                }
                is MovieDetailState.Loading -> {
                    // 로딩 상태 설정
                    handleLoadingState()
                }
                is MovieDetailState.Success -> {
                    // 완료 상태 설정
                    handleSuccessState(it)
                }
                is MovieDetailState.Error -> {
                    // 에러 상태 설정
                    handleErrorState()
                }
                is MovieDetailState.PersonSearchSuccess -> {
                    // 배우 상세 조회 완료 상태 설정
                    handlePersonSearchSuccess(it)
                }
                is MovieDetailState.PersonSearchError -> {
                    // 배우 상세 조회 에러 상태 설정
                    handlePersonSearchError()
                }
            }
        }
    }

    /**
     * 각종 뷰들 초기화 : 초기화 할 내용이 아직 없음
     * */
    private fun initViews(){}


    /**
     * 북마크 버튼 클릭 리스너
     * */
    fun setBookMarkClickListener(view : View){
        view.tag = if(view.tag.toString() == "true") "false" else "true"
        setViewBookMarkBackground()

        myMovie?.let { myMovie ->
            // Database 값이 있을 시 업데이트
            myMovie.isBookMark = view.tag.toString() == "true"
            viewModel.updateMyMovie(myMovie.isBookMark, myMovie.isLike, myMovie.myVoteAverage?: 0.0f, myMovie.memo ?: "", movieId)
        } ?: run {
            // Database 값이 없을 시 신규 생성
            myMovie = createMyMovie(0.0f, "")
            viewModel.insertMyMovie(myMovie!!)
        }
    }


    /**
     * 좋아요 버튼 클릭 리스너
     * */
    fun setHeartClickListener(view: View){
        view.tag = if(view.tag.toString() == "true") "false" else "true"

        myMovie?.isLike = view.tag.toString() == "true"

        if(view.tag.toString() == "true"){
            // 내 평점, 메모 다이얼로그 생성
            LikeMovieDialog(this@MovieDetailActivity){ myVoteAverage, memo ->
                myMovie?.let { myMovie ->
                    // Database 값이 있을 시 업데이트
                    myMovie.isLike = view.tag.toString() == "true"
                    myMovie.myVoteAverage = myVoteAverage
                    myMovie.memo = memo
                    viewModel.updateMyMovie(myMovie.isBookMark, myMovie.isLike, myVoteAverage, memo, movieId)
                } ?: run {
                    // Database 값이 없을 시 신규 생성
                    myMovie = createMyMovie(myVoteAverage, memo)
                    viewModel.insertMyMovie(myMovie!!)
                }
                setViewHeartBackground()
            }.show()
        }else{
            // Database 값이 있을 시 업데이트
            myMovie?.let { myMovie ->
                viewModel.updateMyMovie(myMovie.isBookMark, myMovie.isLike, myMovie.myVoteAverage?: 0.0f, myMovie.memo ?: "", movieId)
            }
            setViewHeartBackground()
        }
    }


    /**
     * 북마크 버튼 테그 값에 따라 배경 변경
     * */
    private fun setViewBookMarkBackground() {
        binding.viewBookMark.backgroundResource = if(binding.viewBookMark.tag.toString() == "true") R.drawable.ic_bookmark_fill_24 else R.drawable.ic_bookmark_24
    }


    /**
     * 좋아요 버튼 테그 값에 따라 배경 변경
     * */
    private fun setViewHeartBackground() {
        binding.viewHeart.backgroundResource = if(binding.viewHeart.tag.toString() == "true") R.drawable.ic_heart_fill_24 else R.drawable.ic_heart_24
    }


    /**
     * Database 값이 없을 시 신규 생성
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
     * 로딩 상태 설정
     * */
    private fun handleLoadingState() = with(binding){
        progressBar.isVisible = true
    }


    /**
     * 완료 상태 설정
     * */
    private fun handleSuccessState(movieDetailState: MovieDetailState.Success) = with(binding){
        progressBar.isVisible = false
        // 완료 상태 시 보여줄 뷰 활성화 설정
        successVisibleViews()

        movieId = movieDetailState.movieId
        movieDetail = movieDetailState.movieDetail
        myMovie = movieDetailState.myMovie

        // 선택한 영화 정보 저장 (홈 화면에서 추천 영화 조회 시 사용)
        pref.edit{
            putLong(Constants.PREF_KEY_MOVIE_ID, movieId ?: 0)
        }

        // 각종 뷰들 값 셋팅
        bindViews(movieDetailState)

    }


    /**
     * 각종 뷰들 값 셋팅
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
            // 배우 상세 조회
            viewModel.getSearchPersonInfo(name, id)
        }
        (rvCast.adapter as? CastInfoAdapter)?.submitList(movieDetailState.creditsList.cast)

        viewBookMark.tag = if(movieDetailState.myMovie?.isBookMark == true) "true" else "false"
        viewHeart.tag = if(movieDetailState.myMovie?.isLike == true) "true" else "false"
        setViewBookMarkBackground()
        setViewHeartBackground()
    }


    /**
     * 에러 상태 설정
     * */
    private fun handleErrorState() = with(binding){
        progressBar.isVisible = false
        errorVisibleViews()
    }


    /**
     * 완료 상태 시 보여줄 뷰 활성화 설정
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
     * 에러 상태 시 보여줄 뷰 활성화 설정
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
     * 배우 상세 조회 완료 상태 설정
     * todo 화면 아직 미정
     * */
    private fun handlePersonSearchSuccess(movieDetailState: MovieDetailState.PersonSearchSuccess) {
        val personInfo = movieDetailState.personInfo
        Log.e("+++++", "info : ${personInfo.name} / ${personInfo.known_for[0].title}")
    }


    /**
     * 배우 상세 조회 에러 상태 설정
     * todo 화면 아직 미정
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