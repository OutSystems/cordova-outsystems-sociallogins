package com.outsystems.plugins.sociallogins

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class SocialLoginsAppleController {

    fun doLogin(state: String, clientId: String, redirectUri: String, activity: Activity){

        val intent = Intent(activity, AppleSignInActivity::class.java)
        val bundle = Bundle()
        bundle.putString("state", state)
        bundle.putString("clientId", clientId)
        bundle.putString("redirectUri", redirectUri)
        intent.putExtras(bundle)

        activity.startActivityForResult(intent, APPLE_SIGNIN_REQUEST_CODE)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, intent: Intent,
                             onSuccess : (UserInfo) -> Unit, onError : (SocialLoginError) -> Unit) {

        when(resultCode) {
            0 -> {
                onError(SocialLoginError.LOGIN_CANCELLED_ERROR)
            }
            10 -> {
                onError(SocialLoginError.APPLE_INVALID_TOKEN_ERROR)
            }
            11 -> {
                onError(SocialLoginError.APPLE_SIGN_IN_GENERAL_ERROR)
            }

            else -> {

                if(intent.extras == null ||
                    intent.extras?.getString("id")?.isNotEmpty() == true ||
                    intent.extras?.getString("email")?.isNotEmpty() == true ||
                    intent.extras?.getString("token")?.isNotEmpty() == true ) {
                        onError(SocialLoginError.APPLE_SIGN_IN_GENERAL_ERROR)
                }

                val returnBundle = intent.extras!!
                onSuccess(UserInfo(
                    returnBundle.getString("id"),
                    returnBundle.getString("email"),
                    returnBundle.getString("firstName"),
                    returnBundle.getString("lastName"),
                    returnBundle.getString("token")
                ))

            }
        }

    }

    companion object {
        const val APPLE_SIGNIN_REQUEST_CODE = 13
    }

}