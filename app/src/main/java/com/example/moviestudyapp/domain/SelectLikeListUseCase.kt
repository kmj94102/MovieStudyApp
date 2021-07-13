package com.example.moviestudyapp.domain

import com.example.moviestudyapp.data.repository.MovieRepository

class SelectLikeListUseCase(
    private val movieRepository: MovieRepository
) : UseCase {

    suspend operator fun invoke() =
        movieRepository.selectLikeList()

}