package com.example.moviestudyapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moviestudyapp.adapter.TrendingAdapter
import com.example.moviestudyapp.databinding.FragmentHomeBinding
import com.example.moviestudyapp.network.RetrofitUtil
import com.example.moviestudyapp.network.TrendingList
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class HomeFragment : Fragment(), CoroutineScope {

    private var binding: FragmentHomeBinding? = null
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        FragmentHomeBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        binding!!.vpTrending.apply {
            adapter = TrendingAdapter(requireActivity(), convertList).apply {
                setListener{ view ->
                    val movieId = view.tag as Long?
                    val intent = Intent(requireContext(), MovieDetailActivity::class.java)
                    intent.putExtra(Constants.INTENT_MOVIE_ID, movieId)
                    startActivity(intent)
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