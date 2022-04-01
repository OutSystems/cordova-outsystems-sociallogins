package com.outsystems.plugins.sociallogins.linkedin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.outsystems.plugins.sociallogins.OAuthActivity
import com.outsystems.plugins.sociallogins.SocialLoginError
import com.outsystems.plugins.sociallogins.UserInfo

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

        activity.startActivityForResult(intent, LinkedInConstants.LINKEDIN_REQUEST_CODE)
    }

    fun handleActivityResult(resultCode: Int, intent: Intent?,
                             onSuccess : (UserInfo) -> Unit,
                             onError : (SocialLoginError) -> Unit) {

        intent?.extras?.let { extras ->
            extras.let {
                var errorCode = extras.getInt("errorCode")
                if (errorCode == 0) {

                    if (it.getString("id")!!.isEmpty()) {
                        onError(SocialLoginError.LINKEDIN_SIGN_IN_MISSING_USER_ID)
                    } else if (it.getString("token")!!.isEmpty()) {
                        onError(SocialLoginError.LINKEDIN_MISSING_ACCESS_TOKEN_ERROR)
                    } else {
                        onSuccess(
                            UserInfo(
                            it.getString("id"),
                            it.getString("email") ?: "",
                            it.getString("firstName"),
                            it.getString("lastName"),
                            it.getString("token"),
                            it.getString("picture")
                        )
                        )
                    }

                } else {
                    SocialLoginError.valueOf(errorCode)?.let { error ->
                        onError(error)
                    } ?: run {
                        onError(SocialLoginError.LINKEDIN_SIGN_IN_GENERAL_ERROR)
                    }
                }
            }
        } ?: run {
            if (resultCode == 0) {
                onError(SocialLoginError.LOGIN_CANCELLED_ERROR)
            } else {
                onError(SocialLoginError.APPLE_SIGN_IN_GENERAL_ERROR)
            }
        }
    }

}
