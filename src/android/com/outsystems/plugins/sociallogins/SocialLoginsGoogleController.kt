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


class SocialLoginsGoogleController(var activity : Activity, var context : Context) {


    private val GOOGLE_SIGN_IN: Int = 2

    fun doLoginGoogle(){

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);

        //start the login flow:
        signIn(mGoogleSignInClient)
    }

    fun doLogoutGoogle(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)
        signOut(mGoogleSignInClient)
    }

    fun isUserLoggedIn() : Pair<Boolean, GoogleSignInAccount?>{
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if(account != null){
            return Pair(true, account)
        }
        return Pair(false, null)
    }

/*
    fun getLoginData(onSuccess : (LoginDataResponse) -> Unit, onError : (SocialLoginError) -> Unit) {

        val isLoggedIn = isUserLoggedIn()
        if(isLoggedIn.first){
            onSuccess(LoginDataResponse(
                isLoggedIn.second!!.id,
                isLoggedIn.second!!.email,
                isLoggedIn.second!!.displayName,
                isLoggedIn.second!!.photoUrl.toString()
                )
            )
        }
        else{
            onError(SocialLoginError.NO_USER_LOGGED_IN_ERROR)
        }
    }
    */

    private fun signIn(signInClient: GoogleSignInClient) {
        val signInIntent: Intent = signInClient.signInIntent
        startActivityForResult(activity, signInIntent, GOOGLE_SIGN_IN, null)
    }

    private fun signOut(signInClient: GoogleSignInClient) {
        signInClient.signOut()
            .addOnCompleteListener(activity, OnCompleteListener {
                //do nothing, probably send plugin result with success using addOnSuccessListener
                //also add a addOnFailureListener
            })
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode === 2) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            val email = account.email
            val displayName = account.displayName
            // Signed in successfully, show authenticated UI.
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign In:", "signInResult:failed code=" + e.statusCode)
        }
    }


}