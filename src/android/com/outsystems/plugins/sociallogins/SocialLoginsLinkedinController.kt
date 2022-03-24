package com.outsystems.plugins.sociallogins

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat.startActivityForResult

class SocialLoginsLinkedinController {

    object LinkedinConstants {
        const val LINKEDIN_REQUEST_CODE = 3
    }

    fun doLogin(state: String, clientId: String, redirectUri: String, activity: Activity){

        val intent = Intent(activity, LinkedInSignInActivity::class.java)
        val bundle = Bundle()
        bundle.putString("state", state)
        bundle.putString("clientId", clientId)
        bundle.putString("redirectUri", redirectUri)
        intent.putExtras(bundle)

        activity?.startActivityForResult(intent, LinkedinConstants.LINKEDIN_REQUEST_CODE)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, intent: Intent,
                             onSuccess : (UserInfo) -> Unit, onError : (SocialLoginError) -> Unit) {

        val returnBundle = intent.extras

        returnBundle?.let {

            if (it.getString("id")!!.isEmpty()) {
                onError(SocialLoginError.LINKEDIN_SIGN_IN_MISSING_USER_ID)
            } else if (it.getString("token")!!.isEmpty()) {
                onError(SocialLoginError.LINKEDIN_MISSING_ACCESS_TOKEN_ERROR)
            } else if (it.getString("email")!!.isEmpty()) {
                onError(SocialLoginError.LINKEDIN_SIGN_IN_MISSING_EMAIL)
            } else {
                onSuccess(UserInfo(
                    it.getString("id"),
                    it.getString("email"),
                    it.getString("firstName"),
                    it.getString("lastName"),
                    it.getString("token"),
                    it.getString("picture")
                ))
            }

        } ?: run {
            onError(SocialLoginError.LINKEDIN_SIGN_IN_GENERAL_ERROR)
        }
    }

}