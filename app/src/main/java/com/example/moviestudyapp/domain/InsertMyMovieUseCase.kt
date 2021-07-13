package com.example.moviestudyapp.domain

import com.example.moviestudyapp.data.entity.MyMovie
import com.example.moviestudyapp.data.repository.MovieRepository

class InsertMyMovieUseCase(
    private val movieRepository: MovieRepository
) : UseCase {

    suspend operator fun invoke(myMovie: MyMovie) =
        movieRepository.insertMyMovie(myMovie)

}