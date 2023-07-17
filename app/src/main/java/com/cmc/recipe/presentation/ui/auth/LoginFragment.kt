package com.cmc.recipe.presentation.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.cmc.recipe.BuildConfig
import com.cmc.recipe.R
import com.cmc.recipe.databinding.FragmentLoginBinding
import com.cmc.recipe.presentation.MainActivity
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate){

    private val googleSignInClient: GoogleSignInClient by lazy { getGoogleClient() }
    private lateinit var googleAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainActivity = activity as MainActivity
        mainActivity.hideToolbar(true)
        mainActivity.hideBottomNavigation(true)
    }

    override fun initFragment() {
        googleAuth = FirebaseAuth.getInstance()
        initListener()
    }

    private fun initListener(){
        binding.btnGoogleLogin.setOnClickListener {
            googleSignInClient.signOut()
            val signInIntent = googleSignInClient.signInIntent
            googleAuthLauncher.launch(signInIntent)
        }
    }

    private val googleAuthLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

        try {
            val account = task.getResult(ApiException::class.java)

            Log.d("account",account.id.toString())
            Log.d("userName",account.displayName.toString())
            Log.d("idToken",account.idToken.toString())

            firebaseAuthWithGoogle(account)

        } catch (e: ApiException) {
            Log.e(LoginFragment::class.java.simpleName, e.stackTraceToString())
        }
    }

    private fun getGoogleClient(): GoogleSignInClient {
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
          //  .requestScopes(Scope("https://www.googleapis.com/auth/pubsub"))
            .requestServerAuthCode("${BuildConfig.CLIENT_ID}") // server authcode를 요청
            .requestIdToken("${BuildConfig.CLIENT_ID}")
            .requestEmail() // 이메일 요청
            .build()

        return GoogleSignIn.getClient(requireActivity(), googleSignInOption)
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount){
        Log.d("firebaseAuthWithGoogle", account.id!!)
        Log.d("idToken", account.idToken!!)

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        googleAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if(task.isSuccessful){
                googleAuth.currentUser?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                    if(tokenTask.isSuccessful()){
                        val accessToken = tokenTask.result.token
                        Log.d("accessToken","${accessToken}")
                        if(task.result.additionalUserInfo?.isNewUser == true){
                            moveSignUpFragment()
                        }else{
                            // 사용자가 회원가입 후 간편로그인 클릭 했을 때 홈화면으로 바로 이동
                        }
                    }
                }
            }else{
                Log.e("isError",task.toString())
            }
        }
    }

    private fun moveSignUpFragment() {
        findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
    }
}