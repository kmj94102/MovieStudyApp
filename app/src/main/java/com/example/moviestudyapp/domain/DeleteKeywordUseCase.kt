package com.example.moviestudyapp.domain

import com.example.moviestudyapp.data.repository.MovieRepository

class DeleteKeywordUseCase(
    private val movieRepository : MovieRepository
) : UseCase {

    suspend operator fun invoke(keyword : String) =
        movieRepository.deleteKeyword(keyword)

}