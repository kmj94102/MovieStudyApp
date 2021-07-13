package com.example.moviestudyapp.domain

import com.example.moviestudyapp.data.repository.MovieRepository

class SelectBookMarkListUseCase(
    private val movieRepository: MovieRepository
) : UseCase {

    suspend operator fun invoke() =
        movieRepository.selectBookMarkList()

}