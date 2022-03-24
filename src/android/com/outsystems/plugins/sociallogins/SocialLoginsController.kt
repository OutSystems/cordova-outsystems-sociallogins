package com.outsystems.plugins.sociallogins

import android.app.Activity
import android.content.Intent

class SocialLoginsController(var appleController: SocialLoginsAppleController,
                             var googleController: SocialLoginsGoogleController,
                             var linkedinController: SocialLoginsLinkedinController) {

    fun doLoginApple(state: String, clientId: String, redirectUri: String, activity: Activity){
        appleController.doLogin(state, clientId, redirectUri, activity)
    }

    fun doLoginGoogle(activity: Activity){
        googleController.doLogin(activity)
    }

    fun doLoginLinkedin(state: String, clientId: String, redirectUri: String, activity: Activity){
        linkedinController.doLogin(state, clientId, redirectUri, activity)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, intent: Intent,
                             onSuccess : (UserInfo) -> Unit, onError : (SocialLoginError) -> Unit) {

        if(resultCode == 1){
            //call apple
            appleController.handleActivityResult(requestCode, resultCode, intent,
                {
                    onSuccess(it)
                }, {
                    onError(it)
                }
            )
        }
        else if(resultCode == 2){ //call google
            googleController.handleActivityResult(requestCode, resultCode, intent,
                {
                    onSuccess(it)
                },
                {
                    onError(it)
                })
        }
        else if(resultCode == 3){ //call linkedin
            linkedinController.handleActivityResult(requestCode, resultCode, intent,
                {
                    onSuccess(it)
                },
                {
                    onError(it)
                })
        }

    }

}