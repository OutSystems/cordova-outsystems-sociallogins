package com.outsystems.plugins.sociallogins

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

class SocialLoginsAppleController(var activity : Activity, var context : Context) {

    private val APPLESIGNIN: Int = 13;

    fun doLogin(state: String, clientId: String, redirectUri: String){

        val intent = Intent(activity, AppleSignInActivity::class.java)
        val bundle = Bundle()
        bundle.putString("state", state)
        bundle.putString("clientId", clientId)
        bundle.putString("redirectUri", redirectUri)
        intent.putExtras(bundle)

        activity?.startActivityForResult(intent, APPLESIGNIN)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, intent: Intent,
                             onSuccess : (UserInfo) -> Unit, onError : (SocialLoginError) -> Unit) {

        val returnBundle = intent.extras
        if (returnBundle != null) {
            onSuccess(UserInfo(
                returnBundle.getString("id"),
                returnBundle.getString("email"),
                returnBundle.getString("firstName"),
                returnBundle.getString("lastName"),
                returnBundle.getString("token")
            ))
        }
        else{
            onError(SocialLoginError.APPLE_SIGN_IN_GENERAL_ERROR)
        }
    }

}