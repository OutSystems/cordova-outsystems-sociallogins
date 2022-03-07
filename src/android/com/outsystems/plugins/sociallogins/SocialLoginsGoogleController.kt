package com.outsystems.plugins.sociallogins

import android.app.Activity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

import android.content.Context
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener

class SocialLoginsGoogleController {

    private val GOOGLE_SIGN_IN: Int = 2

    fun doLogin(activity: Activity){

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);

        //start the login flow:
        signIn(mGoogleSignInClient, activity)
    }

    //Maybe we could keep this to avoid asking the user to login if he/she is already logged in and we can just return the data back
    fun isUserLoggedIn(context: Context) : Pair<Boolean, GoogleSignInAccount?>{
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if(account != null){
            return Pair(true, account)
        }
        return Pair(false, null)
    }

    /*
    fun getLoginData(onSuccess : (UserInfo) -> Unit, onError : (SocialLoginError) -> Unit) {

        val isLoggedIn = isUserLoggedIn()
        if(isLoggedIn.first){
            onSuccess(UserInfo(
                isLoggedIn.second!!.id,
                isLoggedIn.second!!.email,
                isLoggedIn.second!!.displayName,
                isLoggedIn.second!!.photoUrl.toString(),
                "" // DO WE NEED THIS FOR GOOGLE? DONT THINK SO
            )
            )
        }
        else{
            onError(SocialLoginError.NO_USER_LOGGED_IN_ERROR)
        }
    }
     */

    private fun signIn(signInClient: GoogleSignInClient, activity: Activity) {
        val signInIntent: Intent = signInClient.signInIntent
        startActivityForResult(activity, signInIntent, GOOGLE_SIGN_IN, null)
    }


    fun handleActivityResult(requestCode: Int, resultCode: Int, intent: Intent,
                             onSuccess : (UserInfo) -> Unit, onError : (SocialLoginError) -> Unit) {

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode === 2) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
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
            onSuccess(
                UserInfo(
                    account.id,
                    account.email,
                    account.givenName,
                    account.familyName,
                    account.idToken, // this is coming null because we need to request it, but for that we need the server ClientID...
                    account.photoUrl.toString()
                ))
            // Signed in successfully, show authenticated UI.
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign In:", "signInResult:failed code=" + e.statusCode)
            onError(SocialLoginError.GOOGLE_SIGN_IN_GENERAL_ERROR)
        }
    }


}