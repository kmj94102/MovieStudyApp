package com.example.moviestudyapp.network

data class Movie(
    val backdrop_path : String?,
    val overview : String?
)

data class TrendingListResult(
    val page : Long?,
    val results : List<TrendingList>,
    val total_pages : Long?,
    val total_results : Long?
)

data class TrendingList(
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

data class SimilarListResult(
    val results : List<SimilarList>
)

data class SimilarList(
    val video : Boolean?,
    val title : String?,
    val overview : String?,
    val release_date : String?,
    val vote_count : Long?,
    val adult : Boolean?,
    val backdrop_path : String?,
    val id : Long?,
    val genre_ids : List<Int>,
    val vote_average : Float?,
    val poster_path : String?
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

data class MovieSearchResult(
    val page : Int,
    val results : List<MovieSearchList>,
    val total_pages : Int,
    val total_results : Int
)

data class MovieSearchList(
    val adult : Boolean?,
    val backdrop_path : String?,
    val genre_ids : List<Int>,
    val id : Long?,
    val original_language : String?,
    val original_title : String?,
    val overview : String?,
    val popularity : Float?,
    val poster_path : String?,
    val release_date : String?,
    val title : String?,
    val video : Boolean?,
    val vote_average : Float?,
    val vote_count : Long?
)

data class PersonSearchResult(
    val page : Int?,
    val results : List<PersonSearchList>,
    val total_pages : Int,
    val total_results : Int
)

data class PersonSearchList(
    val id : Long?,
    val name : String?,
    val gender : Int?,
    val adult : Boolean?,
    val known_for : List<MovieStarringList>,
    val known_for_department : String?,
    val popularity : Float?,
    val profile_path : String?
)

data class MovieStarringList(
    val adult : Boolean?,
    val backdrop_path : String?,
    val genre_ids : List<Int>,
    val id: Long?,
    val media_type : String?,
    val original_language : String?,
    val original_title : String?,
    val overview : String?,
    val poster_path : String?,
    val release_date : String?,
    val title : String?,
    val video : Boolean?,
    val vote_average : Float?,
    val vote_count : Long?
)