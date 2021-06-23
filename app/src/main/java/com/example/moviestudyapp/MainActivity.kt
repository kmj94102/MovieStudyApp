package com.example.moviestudyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.moviestudyapp.adapter.TrendingAdapter
import com.example.moviestudyapp.databinding.ActivityMainBinding
import com.example.moviestudyapp.network.RetrofitUtil
import com.example.moviestudyapp.network.TrendingList
import kotlinx.coroutines.*
import org.jetbrains.anko.startActivity
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private val binding : ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        launch(coroutineContext) {
            try {
                setTrendingAdapter(getTrendingMovieList())
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    private suspend fun getTrendingMovieList() : TrendingList = withContext(Dispatchers.IO){
        RetrofitUtil.movieService.getTrendingMovieList(MEDIA_TYPE, TIME_WINDOW)
    }

    private suspend fun setTrendingAdapter(trendingList : TrendingList) = withContext(coroutineContext){
        val convertList = trendingList.results.map {
            Pair("${Constants.MOVIE_API_START_IMAGE_URL}${it.backdrop_path}", it.id)
        }

        binding.vpTrending.apply {
            adapter = TrendingAdapter(this@MainActivity, convertList).apply {
                setListener{ view ->
                    val movieId = view.tag as Long?
                    startActivity<MovieDetailActivity>(Constants.INTENT_MOVIE_ID to movieId)
                }
                offscreenPageLimit = 3
                setPageTransformer(SliderTransformer(3))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        coroutineContext.cancel()
    }

    companion object{
        const val MEDIA_TYPE = "movie"
        const val TIME_WINDOW = "day"
    }
}