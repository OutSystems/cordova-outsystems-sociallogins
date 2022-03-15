package com.outsystems.plugins.sociallogins

import android.app.Activity
import android.content.Context
import android.content.Intent

class SocialLoginsController(var appleController: SocialLoginsAppleController, var googleController: SocialLoginsGoogleController) {

    fun doLoginApple(state: String, clientId: String, redirectUri: String, activity: Activity){
        appleController.doLogin(state, clientId, redirectUri, activity)
    }

    fun doLoginGoogle(activity: Activity){
        googleController.doLogin(activity)
    }

    fun handleActivityResult(context: Context, requestCode: Int, resultCode: Int, intent: Intent,
                             onSuccess : (UserInfo) -> Unit, onError : (SocialLoginError) -> Unit){

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
        else if(requestCode == 2){ //call google
            googleController.handleActivityResult(context, requestCode, resultCode, intent,
                {
                    onSuccess(it)
                },
                {
                   onError(it)
                })
        }

    }

}