package com.example.moviestudyapp.domain

import com.example.moviestudyapp.data.repository.MovieRepository

internal class GetSearchMoviesUseCase(
    private val movieRepository: MovieRepository
) : UseCase{

    suspend operator fun invoke(query: String?) =
        movieRepository.getSearchMovies(query)

}