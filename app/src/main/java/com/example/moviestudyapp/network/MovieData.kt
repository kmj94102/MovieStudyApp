package com.example.moviestudyapp.network

data class Movie(
    val backdrop_path : String?,
    val overview : String?
)

data class TrendingList(
    val page : Long?,
    val results : List<TrendingListResult>,
    val total_pages : Long?,
    val total_results : Long?
)

data class TrendingListResult(
    val vote_average : Float?,
    val overview : String?,
    val release_date : String?,
    val id : Long?,
    val adult : Boolean?,
    val backdrop_path : String?,
    val genre_ids : List<Int>,
    val vote_count : Long?,
    val original_language : String?,
    val original_title : String?,
    val poster_path : String?,
    val video : Boolean?,
    val title : String?,
    val popularity : Float?,
    val media_type : String?
)

data class MovieDetail(
    val adult : Boolean?,               // 성인
    val backdrop_path : String?,
    val budget : Long?,                 // 예산
    val genres : List<Genre>,
    val homepage : String?,
    val original_language : String?,
    val original_title : String?,
    val overview : String?,             // 개요
    val popularity : Float?,            // 인기
    val poster_path : String?,
    val production_companies : Any?,
    val production_countries : Any?,
    val release_date : String?,         // 출시일
    val revenue : Long?,                // 수입
    val runtime : Long?,
    val spoken_languages : Any?,
    val status : String?,
    val tagline : String?,
    val title : String?,
    val video : Boolean?,
    val vote_average : Float?,          // 평점
    val vote_count : Long?
)

data class Genre(
    val id : Long?,
    val name: String?
)

data class CreditsList(
    val id : Long?,
    val cast : List<CastInfo>
)

data class CastInfo(
    val adult : Boolean?,
    val gender : Int?,
    val id : Long?,
    val known_for_department : String?,
    val name : String?,
    val original_name : String?,
    val popularity : Float?,
    val profile_path : String?,
    val cast_id : Long?,
    val character : String?,
    val credit_id : String?,
    val order : Long?
)