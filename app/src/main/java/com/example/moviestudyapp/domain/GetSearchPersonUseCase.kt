package com.example.moviestudyapp.domain

import com.example.moviestudyapp.data.repository.MovieRepository
import com.example.moviestudyapp.network.PersonSearchResult

class GetSearchPersonUseCase(
    private val movieRepository: MovieRepository
) : UseCase {

    suspend operator fun invoke(name: String?) : PersonSearchResult =
        movieRepository.getSearchPerson(name)

}