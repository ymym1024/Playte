package com.cmc.recipe.utils

import android.content.Context
import android.content.SharedPreferences
import com.cmc.recipe.utils.Constant.PREFS_APP_FILE
import com.cmc.recipe.utils.Constant.USER_ACCESS_TOKEN
import com.cmc.recipe.utils.Constant.USER_REFRESH_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val prefs : SharedPreferences = context.getSharedPreferences(PREFS_APP_FILE,Context.MODE_PRIVATE)

    fun saveAccessToken(token:String){
        prefs.edit().putString(USER_ACCESS_TOKEN,token).apply()
    }

    fun getAccessToken() : String{
        return prefs.getString(USER_ACCESS_TOKEN,"").toString()
    }

    fun saveRefreshToken(token:String){
        prefs.edit().putString(USER_REFRESH_TOKEN,token).apply()
    }

    fun getRefreshToken():String{
        return prefs.getString(USER_REFRESH_TOKEN,"").toString()
    }
}