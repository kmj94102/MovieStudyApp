package com.example.moviestudyapp.domain

import com.example.moviestudyapp.data.entity.MyKeywordEntity
import com.example.moviestudyapp.data.repository.MovieRepository

class InsertKeywordUseCase(
    private val movieRepository: MovieRepository
) : UseCase {

    suspend operator fun invoke(myKeywordEntity: MyKeywordEntity) =
        movieRepository.insertKeyword(myKeywordEntity)

}