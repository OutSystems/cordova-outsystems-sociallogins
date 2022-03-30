package com.outsystems.plugins.sociallogins

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class SocialLoginsLinkedinController {

    fun doLogin(state: String, clientId: String, redirectUri: String, activity: Activity){
        val intent = Intent(activity, OAuthActivity::class.java)
        val bundle = Bundle()
        bundle.putString("state", state)
        bundle.putString("clientId", clientId)
        bundle.putString("redirectUri", redirectUri)
        bundle.putString("scope", LinkedInConstants.SCOPE)
        bundle.putString("authUrl", LinkedInConstants.AUTH_URL)
        bundle.putString("responseType", LinkedInConstants.RESPONSE_TYPE)
        intent.putExtras(bundle)

        activity?.startActivityForResult(intent, LinkedInConstants.LINKEDIN_SIGN_IN_REQUEST_CODE)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, intent: Intent,
                             onSuccess : (UserInfo) -> Unit, onError : (SocialLoginError) -> Unit) {

        val returnBundle = intent.extras
        returnBundle?.let {
            var errorCode = returnBundle.getInt("errorCode")
            if (errorCode == 0) {

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

            } else {
                SocialLoginError.valueOf(errorCode)?.let { error ->
                    onError(error)
                } ?: run {
                    onError(SocialLoginError.LINKEDIN_SIGN_IN_GENERAL_ERROR)
                }
            }

        } ?: run {
            onError(SocialLoginError.LINKEDIN_SIGN_IN_GENERAL_ERROR)
        }

    }

}
