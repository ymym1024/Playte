package com.cmc.recipe.presentation.ui.auth

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.cmc.recipe.BuildConfig
import com.cmc.recipe.MainApplication
import com.cmc.recipe.databinding.FragmentLoginBinding
import com.cmc.recipe.presentation.MainActivity
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.viewmodel.AuthViewModel
import com.cmc.recipe.presentation.viewmodel.GoogleViewModel
import com.cmc.recipe.utils.NetworkState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate){

    private val googleViewModel : GoogleViewModel by viewModels()
    private val authViewModel : AuthViewModel by viewModels()

    private val googleSignInClient: GoogleSignInClient by lazy { getGoogleClient() }
    private lateinit var googleAuth: FirebaseAuth

    override fun initFragment() {
        googleAuth = FirebaseAuth.getInstance()
        initListener()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
    }

    private fun initListener(){
        binding.btnGoogleLogin.setOnClickListener {
            googleSignInClient.signOut()
            val signInIntent = googleSignInClient.signInIntent
            googleAuthLauncher.launch(signInIntent)
        }
    }

    private val googleAuthLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }
    }

    private fun getGoogleClient(): GoogleSignInClient {
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(Scopes.EMAIL))
            .requestServerAuthCode("${BuildConfig.CLIENT_ID}") // server authcode를 요청
            .requestIdToken("${BuildConfig.CLIENT_ID}") // server authcode를 요청
            .requestEmail() // 이메일 요청
            .build()

        return GoogleSignIn.getClient(requireActivity(), googleSignInOption)
    }

    private fun handleSignInResult(completedTask : Task<GoogleSignInAccount>){
        val authCode = completedTask.getResult(ApiException::class.java)?.serverAuthCode
        try {
            lifecycleScope.launch {
                authCode?.run {
                    var accessToken: String
                    googleViewModel.fetchAuthInfo(this)
                    googleViewModel.authResult.collect{ result->
                        when(result){
                            is NetworkState.Success -> {
                                accessToken = result.data.access_token
                                Log.d("accessToken",accessToken)

                                login(accessToken)
                            }
                            is NetworkState.Error -> {
                                showToastMessage(result.message.toString())
                            }
                            else -> {}
                        }
                    }
                }
            }
        }catch (e: ApiException) {
            Log.d("로그인 실패","${e.statusCode}")
        }
    }

    private fun login(accessToken:String){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                authViewModel.login(accessToken)
                authViewModel.loginResult.collect{
                    when(it){
                        is NetworkState.Success -> {
                            it.data?.let { data ->
                                if(data.code == "SUCCESS"){ // TODO : 변경
                                    if(data.data.isMember){
                                        val accessToken = data.data.jwtTokens.accessToken
                                        val refreshToken = data.data.jwtTokens.refreshToken

                                        saveTokens(accessToken,refreshToken)
                                        moveMainActivity()
                                    } else {
                                        moveSignUpFragment(accessToken)
                                    }
                                }else{
                                    Log.d("data","${data.data}")
                                }
                            }
                            authViewModel._loginResult.value = NetworkState.Loading
                        }
                        is NetworkState.Error ->{
                            showToastMessage(it.message.toString())
                            authViewModel._loginResult.value = NetworkState.Loading
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun moveSignUpFragment(accessToken:String) {
        val directions = LoginFragmentDirections.actionLoginFragmentToSignupFragment(accessToken)
        findNavController().navigate(directions)
    }

    private fun moveMainActivity() {
        val intent = Intent(activity,MainActivity::class.java)
        startActivity(intent)
    }

    private fun saveTokens(accessToken: String, refreshToken: String) {
        MainApplication.tokenManager.saveAccessToken(accessToken)
        MainApplication.tokenManager.saveRefreshToken(refreshToken)
    }
}