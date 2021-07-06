package com.example.moviestudyapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviestudyapp.data.entity.MyKeywordEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertKeyword(myKeywordEntity: MyKeywordEntity)

    @Query("SELECT DISTINCT keyword FROM MyKeywordEntity ORDER BY id DESC LIMIT 5")
    suspend fun selectKeywordLists() : List<String>

    @Query("DELETE FROM MyKeywordEntity WHERE keyword=:keyword")
    suspend fun deleteKeyword(keyword : String) : Int

}