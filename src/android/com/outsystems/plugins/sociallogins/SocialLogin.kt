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


class SocialLogin(var activity : Activity, var context : Context) {


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

        val account = GoogleSignIn.getLastSignedInAccount(context)

        if(account != null){
            Log.d("Check for logged in account", "Account not null!")
            val email = account.email
            val displayName = account.displayName
            val String = ""
        }
        else{
            //start the login flow:
            signIn(mGoogleSignInClient)
        }

    }

    private fun signIn(signInClient: GoogleSignInClient) {
        val signInIntent: Intent = signInClient.signInIntent
        startActivityForResult(activity, signInIntent, GOOGLE_SIGN_IN, null)
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