package com.outsystems.plugins.sociallogins

import android.app.Activity
import android.content.Intent

class SocialLoginsController(var controllerApple: SocialLoginsAppleController) {

    fun doLoginApple(state: String, clientId: String, redirectUri: String, activity: Activity){
        controllerApple.doLogin(state, clientId, redirectUri, activity)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, intent: Intent,
                             onSuccess : (UserInfo) -> Unit, onError : (SocialLoginError) -> Unit){

        if(resultCode == 1){
            //call apple
            controllerApple.handleActivityResult(requestCode, resultCode, intent,
                {
                    onSuccess(it)
                }, {
                    onError(it)
                }
            )
        }
        else{
            //other providers, like Google
            //TODO
        }

    }

}