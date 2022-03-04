package com.outsystems.plugins.sociallogins

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class SocialLoginsAppleController {

    private val APPLESIGNIN: Int = 13;

    fun doLogin(state: String, clientId: String, redirectUri: String, activity: Activity){

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

            if(returnBundle.getString("id")!!.isNotEmpty()
                && returnBundle.getString("email")!!.isNotEmpty()
                && returnBundle.getString("token")!!.isNotEmpty()){
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
        else{
            onError(SocialLoginError.APPLE_SIGN_IN_GENERAL_ERROR)
        }
    }

}