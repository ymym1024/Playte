package com.cmc.recipe.presentation.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cmc.recipe.MainApplication
import com.cmc.recipe.databinding.ActivitySplashBinding
import com.cmc.recipe.presentation.ui.auth.AuthActivity
import com.cmc.recipe.presentation.viewmodel.AuthViewModel
import com.cmc.recipe.presentation.viewmodel.UserViewModel
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    private val authViewModel : AuthViewModel by viewModels()
    private val userViewModel : UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        validateLogin()
    }

    fun validateLogin(){
        val refreshToken: String = MainApplication.tokenManager.getRefreshToken()!!

        if(refreshToken == ""){ // 앱 설치 후 최초 로그인
            goLogin()
        }else{
            validateToken()
        }
    }

    fun validateToken(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){

                userViewModel.getMyInfo()
                userViewModel.myInfoResult.collect{
                    when(it){
                        is NetworkState.Success -> {
                            it.data?.let {data ->
                                goMain()
                            }
                            userViewModel._myInfoResult.value = NetworkState.Loading
                        }
                        is NetworkState.Error ->{
                            if(it.code==2003){
                                refreshToken()
                            }
                            userViewModel._myInfoResult.value = NetworkState.Loading
                        }
                        else -> {  }
                    }
                }
            }
        }
    }

    fun refreshToken(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                val refreshToken = MainApplication.tokenManager.getRefreshToken()

                authViewModel.refreshToken(refreshToken)
                authViewModel.refreshResult.collect{
                    when(it){
                        is NetworkState.Success -> {
                            it.data?.let {data ->
                                if(data.code == "SUCCESS"){
                                    val accessToken = data.data.accessToken
                                    val refreshToken = data.data.refreshToken

                                    saveTokens(accessToken,refreshToken)
                                    goMain()
                                }
                            }
                            authViewModel._refreshResult.value = NetworkState.Loading
                        }
                        is NetworkState.Error ->{
                            Log.d("error",it.toString())
                            if(it.code==2005){
                                goLogin()
                            }
                            authViewModel._refreshResult.value = NetworkState.Loading
                        }
                        else -> {  }
                    }
                }
            }
        }
    }

    private fun goLogin(){
        val intent = Intent(this@SplashActivity,AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goMain(){
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun saveTokens(accessToken: String, refreshToken: String) {
        MainApplication.tokenManager.saveAccessToken(accessToken)
        MainApplication.tokenManager.saveRefreshToken(refreshToken)
    }
}