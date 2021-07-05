package com.example.moviestudyapp.domain

import com.example.moviestudyapp.data.repository.MovieRepository

internal class GetMovieDetailUseCase(
    private val movieRepository: MovieRepository
) : UseCase{

    suspend operator fun invoke(movieId : Long?) =
        movieRepository.getMovieDetail(movieId)

}