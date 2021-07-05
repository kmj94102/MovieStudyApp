package com.example.moviestudyapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MyKeywordEntity(
    @PrimaryKey(autoGenerate = true) val id : Long = 0,
    val keyword : String
)
