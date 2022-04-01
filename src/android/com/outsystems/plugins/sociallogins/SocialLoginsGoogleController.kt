package com.outsystems.plugins.sociallogins.google

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.outsystems.plugins.sociallogins.SocialLoginError
import com.outsystems.plugins.sociallogins.UserInfo

class SocialLoginsGoogleController(private var context: Context? = null, private var googleHelper: GoogleHelperInterface) {

    companion object {
        const val GOOGLE_REQUEST_CODE = 2
    }

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
        signInClient.signOut()
        val signInIntent: Intent = signInClient.signInIntent
        startActivityForResult(activity, signInIntent, GOOGLE_REQUEST_CODE, null)
    }


    fun handleActivityResult(resultCode: Int, intent: Intent?,
                             onSuccess : (UserInfo) -> Unit, onError : (SocialLoginError) -> Unit) {
        if (resultCode == 0) {
            onError(SocialLoginError.LOGIN_CANCELLED_ERROR)
        } else {
            try {
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)
                handleSignInResult(task,
                    {
                        onSuccess(it)
                    },
                    {
                        onError(it)
                    })
            }  catch(hse : Exception) {
                onError(SocialLoginError.GOOGLE_SIGN_IN_GENERAL_ERROR)
            }
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
                    else{
                        var email = account.email ?: ""
                        var givenName = account.givenName ?: ""
                        var familyName = account.familyName ?: ""
                        var photoUrl = account.photoUrl?.toString() ?: ""
                        onSuccess(
                            UserInfo(
                                account.id,
                                email,
                                givenName,
                                familyName,
                                it,
                                photoUrl
                            )
                        )
                    }
                },
                {
                    onError(it)
                }
            )
        } catch (e: ApiException) {
            onError(SocialLoginError.GOOGLE_SIGN_IN_GENERAL_ERROR)
        } catch (e: Exception) {
            onError(SocialLoginError.GOOGLE_SIGN_IN_GENERAL_ERROR)
        }
    }

}