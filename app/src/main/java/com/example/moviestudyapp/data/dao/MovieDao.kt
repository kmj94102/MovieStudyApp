package com.example.moviestudyapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviestudyapp.data.entity.MyKeywordEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeyword(myKeywordEntity: MyKeywordEntity)

    @Query("SELECT keyword FROM MyKeywordEntity")
    suspend fun selectKeywordLists() : List<String>

}