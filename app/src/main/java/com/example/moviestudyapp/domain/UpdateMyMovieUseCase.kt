package com.example.moviestudyapp.domain

import com.example.moviestudyapp.data.entity.MyMovie
import com.example.moviestudyapp.data.repository.MovieRepository

class UpdateMyMovieUseCase(
    private val movieRepository: MovieRepository
) : UseCase {

    suspend operator fun invoke(isBookMark: Boolean, isLike: Boolean, movieId : Long?) =
        movieRepository.updateMyMove(isBookMark, isLike, movieId)

}