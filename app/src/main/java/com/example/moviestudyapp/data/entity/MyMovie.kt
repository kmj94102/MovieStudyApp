package com.example.moviestudyapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moviestudyapp.network.Genre
import com.example.moviestudyapp.network.MovieDetail

@Entity
data class MyMovie(
    @PrimaryKey(autoGenerate = true) val id : Long = 0,
    val movieId : Long?,
    var isBookMark : Boolean = false,
    var isLike : Boolean = false,
    var memo : String?,
    var myVoteAverage : Float?,
    // movieDetail
    val backdropPath : String?,
    val genres : String?,
    val posterPath : String?,
    val releaseDate : String?,         // 출시일
    val runtime : Long?,
    val title : String?,
    val voteAverage : Float?,          // 평점
)