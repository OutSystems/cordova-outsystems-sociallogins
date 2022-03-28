package com.outsystems.plugins.sociallogins

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class SocialLoginsAppleController(private val appleTokenValidation: AppleHelperInterface) {
    private var redirectUri: String = ""

    fun doLogin(state: String,
                clientId: String,
                redirectUri: String,
                activity: Activity) {

        val intent = Intent(activity, OAuthActivity::class.java)
        val bundle = Bundle()
        bundle.putString("state", state)
        bundle.putString("clientId", clientId)
        bundle.putString("redirectUri", redirectUri)
        bundle.putString("scope", AppleConstants.SCOPE)
        bundle.putString("authUrl", AppleConstants.AUTH_URL)
        bundle.putString("responseType", AppleConstants.RESPONSE_TYPE)
        intent.putExtras(bundle)

        this.redirectUri = redirectUri

        activity.startActivityForResult(intent, AppleConstants.APPLE_SIGN_IN_REQUEST_CODE)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, intent: Intent,
                             onSuccess : (UserInfo) -> Unit, onError : (SocialLoginError) -> Unit) {

        val returnBundle = intent.extras
        returnBundle?.let {
            val id = returnBundle.getString("id")
            val state = returnBundle.getString("state")
            val email = returnBundle.getString("email")
            val token = returnBundle.getString("token")
            val firstName = returnBundle.getString("firstName")
            val lastName = returnBundle.getString("lastName")

            if (id.isNullOrEmpty()) {
                onError(SocialLoginError.APPLE_MISSING_USER_ID)
            } else if (token.isNullOrEmpty()) {
                onError(SocialLoginError.APPLE_MISSING_ACCESS_TOKEN_ERROR)
            } else if (email.isNullOrEmpty()) {
                onError(SocialLoginError.APPLE_MISSING_USER_EMAIL)
            } else if (state.isNullOrEmpty()) {
                onError(SocialLoginError.APPLE_SIGN_IN_GENERAL_ERROR)
            } else {

                if (!state.isNullOrEmpty()) {
                    appleTokenValidation?.validateToken(id, state, this.redirectUri,
                        {
                            onSuccess(UserInfo(
                                id,
                                email,
                                firstName,
                                lastName,
                                token
                            ))
                        },
                        {
                            onError(it)
                        }
                    )
                }
            }

        } ?: run {
            onError(SocialLoginError.APPLE_SIGN_IN_GENERAL_ERROR)
        }
    }

}