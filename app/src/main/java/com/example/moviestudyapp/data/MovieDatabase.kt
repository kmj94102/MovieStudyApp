package com.example.moviestudyapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moviestudyapp.data.dao.MovieDao
import com.example.moviestudyapp.data.entity.MyKeywordEntity

@Database(
    entities = [MyKeywordEntity::class],
    version = 1
)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao() : MovieDao

    companion object{
        private const val DATABASE_NAME = "movie.db"

        fun build(context: Context) : MovieDatabase =
            Room
                .databaseBuilder(
                    context,
                    MovieDatabase::class.java,
                    DATABASE_NAME
                )
                .build()
    }

}