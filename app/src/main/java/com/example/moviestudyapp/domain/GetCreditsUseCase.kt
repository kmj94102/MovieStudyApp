package com.example.moviestudyapp.domain

import com.example.moviestudyapp.data.repository.MovieRepository

internal class GetCreditsUseCase(
    private val movieRepository: MovieRepository
) : UseCase{

    suspend operator fun invoke(movieId : Long?) =
        movieRepository.getCredits(movieId)

}