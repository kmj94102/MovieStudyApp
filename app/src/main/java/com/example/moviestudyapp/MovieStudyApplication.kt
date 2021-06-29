package com.example.moviestudyapp

import android.app.Application
import com.example.moviestudyapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MovieStudyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(
                Level.ERROR
            )
            androidContext(this@MovieStudyApplication)
            modules(appModule)
        }

    }

}