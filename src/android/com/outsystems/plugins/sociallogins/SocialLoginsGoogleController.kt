package com.outsystems.plugins.sociallogins

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SocialLoginsGoogleController(private var context: Context? = null, private var googleHelper: GoogleHelperInterface) {

    private val GOOGLE_SIGN_IN: Int = 2

    fun doLogin(activity: Activity){

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestId()
            .requestProfile()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);

        signIn(mGoogleSignInClient, activity)
    }

    private fun signIn(signInClient: GoogleSignInClient, activity: Activity) {
        val signInIntent: Intent = signInClient.signInIntent
        startActivityForResult(activity, signInIntent, GOOGLE_SIGN_IN, null)
    }


    fun handleActivityResult(requestCode: Int, resultCode: Int, intent: Intent,
                             onSuccess : (UserInfo) -> Unit, onError : (SocialLoginError) -> Unit) {
        if (requestCode === 2) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)
            handleSignInResult(task,
                {
                    onSuccess(it)
                },
                {
                    onError(it)
                })
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>,
                                   onSuccess : (UserInfo) -> Unit, onError : (SocialLoginError) -> Unit) {

        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)

            googleHelper.getAccessToken(context, account,
                {
                    if(account.id.isNullOrEmpty()){
                        onError(SocialLoginError.GOOGLE_MISSING_USER_ID)
                    }
                    else if (account.email.isNullOrEmpty()){
                        onError(SocialLoginError.GOOGLE_SIGN_IN_GENERAL_ERROR)
                    }
                    else{
                        onSuccess(
                            UserInfo(
                                account.id,
                                account.email,
                                account.givenName,
                                account.familyName,
                                it,
                                account.photoUrl.toString()
                            )
                        )
                    }
                },
                {
                    onError(it)
                }
            )
        } catch (e: ApiException) {
            Log.w("Google Sign In:", "signInResult:failed code=" + e.statusCode)
            onError(SocialLoginError.GOOGLE_SIGN_IN_GENERAL_ERROR)
        } catch (e: Exception) {
            e.message?.let { Log.d("Exception", it) }
            onError(SocialLoginError.GOOGLE_SIGN_IN_GENERAL_ERROR)
        }
    }


}