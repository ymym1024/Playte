package com.cmc.recipe

import android.app.Application
import com.cmc.recipe.utils.TokenManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application(){

    companion object{
        lateinit var tokenManager: TokenManager
    }

    override fun onCreate() {
        super.onCreate()
        tokenManager = TokenManager(applicationContext)
    }
}