package com.example.moviestudyapp.domain

import com.example.moviestudyapp.data.repository.MovieRepository

class SelectKeywordListsUseCase(
    private val movieRepository: MovieRepository
) : UseCase {

    suspend operator fun invoke() =
        movieRepository.selectKeywordLists()

}