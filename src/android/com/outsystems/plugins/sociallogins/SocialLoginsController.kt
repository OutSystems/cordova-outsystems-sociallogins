package com.outsystems.plugins.sociallogins

import android.app.Activity
import android.content.Intent
import com.outsystems.plugins.sociallogins.apple.AppleConstants
import com.outsystems.plugins.sociallogins.apple.SocialLoginsAppleController
import com.outsystems.plugins.sociallogins.facebook.SocialLoginsFacebookController
import com.outsystems.plugins.sociallogins.google.SocialLoginsGoogleController
import com.outsystems.plugins.sociallogins.linkedin.LinkedInConstants
import com.outsystems.plugins.sociallogins.linkedin.SocialLoginsLinkedinController

class SocialLoginsController(private var appleController: SocialLoginsAppleController?,
                             private var googleController: SocialLoginsGoogleController?,
                             private var linkedinController: SocialLoginsLinkedinController?,
                             private var facebookController: SocialLoginsFacebookController?) {

    fun doLoginApple(state: String, clientId: String, redirectUri: String, activity: Activity){
        appleController?.doLogin(state, clientId, redirectUri, activity)
    }
    fun doLoginGoogle(activity: Activity){
        googleController?.doLogin(activity)
    }
    fun doLoginFacebook() {
        facebookController?.doLogin()
    }
    fun doLoginLinkedin(state: String, clientId: String, redirectUri: String, activity: Activity){
        linkedinController?.doLogin(state, clientId, redirectUri, activity)
    }

    fun handleActivityResult(requestCode: Int,
                             resultCode: Int,
                             intent: Intent?,
                             onSuccess : (UserInfo) -> Unit,
                             onError : (SocialLoginError) -> Unit){

        when (requestCode) {
            AppleConstants.APPLE_REQUEST_CODE -> {
                appleController?.handleActivityResult(resultCode, intent,
                    {
                        onSuccess(it)
                    }, {
                        onError(it)
                    }
                )
            }
            SocialLoginsGoogleController.GOOGLE_REQUEST_CODE -> {
                googleController?.handleActivityResult(resultCode, intent,
                    {
                        onSuccess(it)
                    },
                    {
                        onError(it)
                    })
            }
            LinkedInConstants.LINKEDIN_REQUEST_CODE -> {
                linkedinController?.handleActivityResult(resultCode, intent,
                    {
                        onSuccess(it)
                    },
                    {
                        onError(it)
                    })
            }
            SocialLoginsFacebookController.FACEBOOK_REQUEST_CODE -> {
                facebookController?.handleActivityResult(requestCode, resultCode, intent,
                    {
                        onSuccess(it)
                    },
                    {
                        onError(it)
                    }
                )
            }
        }
    }
}