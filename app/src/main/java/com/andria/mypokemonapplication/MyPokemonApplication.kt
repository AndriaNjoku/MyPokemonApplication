package com.andria.mypokemonapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyPokemonApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

}