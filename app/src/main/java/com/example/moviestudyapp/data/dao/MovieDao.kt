package com.example.moviestudyapp.data.dao

import androidx.room.*
import com.example.moviestudyapp.data.entity.MyKeywordEntity
import com.example.moviestudyapp.data.entity.MyMovie

@Dao
interface MovieDao {

    /**
     * 키워드 관련 쿼리
     * */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertKeyword(myKeywordEntity: MyKeywordEntity)

    @Query("SELECT DISTINCT keyword FROM MyKeywordEntity ORDER BY id DESC LIMIT 5")
    suspend fun selectKeywordLists() : List<String>

    @Query("DELETE FROM MyKeywordEntity WHERE keyword=:keyword")
    suspend fun deleteKeyword(keyword : String) : Int


    /**
     * 마이페이지 관련 쿼리
     * */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMyMovie(myMovie: MyMovie)

    @Query("UPDATE MyMovie SET isBookMark = :isBookMark, isLike = :isLike WHERE movieId = :movieId")
    suspend fun updateMyMove(isBookMark: Boolean, isLike: Boolean, movieId : Long?)

    @Query("SELECT * FROM MyMovie WHERE movieId=:movieId")
    suspend fun selectMyMovie(movieId : Long?) : MyMovie?

    @Query("SELECT * FROM MyMovie WHERE isBookMark=:isBookMark ORDER BY id DESC")
    suspend fun selectBookMarkList(isBookMark : Boolean = true) : List<MyMovie>

    @Query("SELECT * FROM MyMovie WHERE isLike=:isLike ORDER BY id DESC")
    suspend fun selectLikeList(isLike : Boolean = true) : List<MyMovie>

}