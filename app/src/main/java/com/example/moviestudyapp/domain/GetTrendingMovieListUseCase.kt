package com.example.moviestudyapp.domain

import com.example.moviestudyapp.data.repository.MovieRepository

internal class GetTrendingMovieListUseCase(
    private val movieRepository: MovieRepository
) : UseCase{

    suspend operator fun invoke(mediaType : String?, timeWindow : String?) =
        movieRepository.getTrendingMovieList(mediaType, timeWindow)

}