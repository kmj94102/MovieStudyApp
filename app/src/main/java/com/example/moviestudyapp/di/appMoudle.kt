package com.example.moviestudyapp.di

import com.example.moviestudyapp.BuildConfig
import com.example.moviestudyapp.Constants
import com.example.moviestudyapp.data.repository.MovieRepository
import com.example.moviestudyapp.data.repository.MovieRepositoryImpl
import com.example.moviestudyapp.domain.GetCreditsUseCase
import com.example.moviestudyapp.domain.GetMovieDetailUseCase
import com.example.moviestudyapp.domain.GetSearchMoviesUseCase
import com.example.moviestudyapp.domain.GetSimilarMovieListUseCase
import com.example.moviestudyapp.domain.GetTrendingMovieListUseCase
import com.example.moviestudyapp.network.MovieApi
import com.example.moviestudyapp.presentation.home.HomeViewModel
import com.example.moviestudyapp.presentation.movie_detail.MovieDetailViewModel
import com.example.moviestudyapp.presentation.search.SearchMovieViewModel
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

internal val appModule = module {

    single { Dispatchers.Main }
    single { Dispatchers.IO }

    // Api
    single {
        OkHttpClient()
            .newBuilder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if(BuildConfig.DEBUG){
                        HttpLoggingInterceptor.Level.BODY
                    }else{
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()
    }
    single<MovieApi> {
        Retrofit.Builder()
            .baseUrl(Constants.MOVIE_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create()
    }

    // Repository
    single<MovieRepository> { MovieRepositoryImpl(get(), get()) }

    // UseCase
    factory { GetTrendingMovieListUseCase(get()) }
    factory { GetSimilarMovieListUseCase(get()) }
    factory { GetMovieDetailUseCase(get()) }
    factory { GetCreditsUseCase(get()) }
    factory { GetSearchMoviesUseCase(get()) }

    // ViewModel
    viewModel { (movieId : Long?) -> HomeViewModel(movieId, get(), get()) }
    viewModel { (movieId : Long?) -> MovieDetailViewModel(movieId, get(), get()) }
    viewModel { SearchMovieViewModel(get()) }

}