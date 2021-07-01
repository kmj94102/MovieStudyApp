package com.example.moviestudyapp.network

enum class GenreEnum(code : Int, name: String) {
    ACTION(28, "액션"),
    ADVENTURE(12, "모험"),
    ANIMATION(16, "애니메이션"),
    COMEDY(35, "코미디"),
    CRIME(80, "범죄"),
    DOCUMENTARY(99, "다큐멘터리"),
    DRAMA(18, "드라마"),
    FAMILY(10751, "가족"),
    FANTASY(14, "판타지"),
    HISTORY(36, "역사"),
    HORROR(27, "공포"),
    MUSIC(10402, "음악"),
    MYSTERY(9648, "미스터리"),
    ROMANCE(10749, "로맨스"),
    SF(878, "SF"),
    TV_MOVIE(10700, "TV 영화"),
    THRILLER(53, "스릴러"),
    WAR(10752, "전쟁"),
    WESTERN(37, "서부");

    val genreName = name
}