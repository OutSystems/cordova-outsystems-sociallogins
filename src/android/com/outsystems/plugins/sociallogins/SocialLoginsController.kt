package com.outsystems.plugins.sociallogins

import android.app.Activity
import android.content.Intent
import com.outsystems.plugins.sociallogins.facebook.SocialLoginsFacebookController

class SocialLoginsController(private var controllerApple: SocialLoginsAppleController,
                             private var controllerFacebook: SocialLoginsFacebookController) {

    fun doLoginApple(state: String, clientId: String, redirectUri: String, activity: Activity){
        //controllerApple.doLogin(state, clientId, redirectUri, activity)
        controllerFacebook.doLogin(activity)
    }

    fun doLoginFacebook(activity: Activity) {
        controllerFacebook.doLogin(activity)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, intent: Intent,
                             onSuccess : (UserInfo) -> Unit, onError : (SocialLoginError) -> Unit){
        when(requestCode) {
            SocialLoginsAppleController.APPLE_SIGNIN_REQUEST_CODE -> {
                //call apple
                controllerApple.handleActivityResult(requestCode, resultCode, intent,
                    {
                        onSuccess(it)
                    }, {
                        onError(it)
                    }
                )
            }
            SocialLoginsFacebookController.FACEBOOK_SIGNIN_REQUEST_CODE -> {
                controllerFacebook.handleActivityResult(requestCode, resultCode, intent,
                    {
                        onSuccess(it)
                    }, {
                        onError(it)
                    }
                )
            }
            else -> {
                //other providers, like Google
                //TODO
            }
        }
    }

}