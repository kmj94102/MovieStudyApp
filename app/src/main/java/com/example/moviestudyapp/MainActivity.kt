package com.example.moviestudyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.moviestudyapp.adapter.TrendingAdapter
import com.example.moviestudyapp.databinding.ActivityMainBinding
import com.example.moviestudyapp.network.RetrofitUtil
import com.example.moviestudyapp.network.TrendingList
import kotlinx.coroutines.*
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private val binding : ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

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
        RetrofitUtil.movieService.getTrendingMovieList("movie", "day")
    }

    private suspend fun setTrendingAdapter(trendingList : TrendingList) = withContext(coroutineContext){
        val imagePathList = trendingList.results.map {
            "${Constants.MOVIE_API_START_IMAGE_URL}${it.backdrop_path}"
        }

        binding.vpTrending.apply {
            adapter = TrendingAdapter(this@MainActivity, imagePathList).apply {
                setListener{ view ->
                    val position = view.tag as Int
                    val movieId = trendingList.results[position].id
                }
                offscreenPageLimit = 3
                setPageTransformer(SliderTransformer(3))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancel()
    }
}