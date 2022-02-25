package com.outsystems.plugins.sociallogins

import android.content.Intent
import com.google.gson.Gson


class SocialLoginsController(var controllerApple: SocialLoginsAppleController) {

    val gson by lazy { Gson() }

    fun doLoginApple(state: String, clientId: String, redirectUri: String){
        controllerApple.doLogin(state, clientId, redirectUri)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, intent: Intent,
                             onSuccess: (String) -> Unit, onError: (Pair<Int,String>) -> Unit){

        if(resultCode == 1){
            //call apple
            controllerApple.handleActivityResult(requestCode, resultCode, intent,
                {
                    userInfoResponse ->
                        val pluginResponseJson = gson.toJson(userInfoResponse)
                    onSuccess(pluginResponseJson)
                }, {
                    error ->
                        onError(Pair(error.code, error.message))
                }
            )
        }
        else{
            //other providers, like Google
            //TODO
        }

    }

}