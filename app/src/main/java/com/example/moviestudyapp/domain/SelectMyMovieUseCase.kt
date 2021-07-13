package com.example.moviestudyapp.domain

import com.example.moviestudyapp.data.repository.MovieRepository

class SelectMyMovieUseCase(
    private val movieRepository: MovieRepository
) : UseCase {

    suspend operator fun invoke(movieId : Long?) =
        movieRepository.selectMyMovie(movieId)

}