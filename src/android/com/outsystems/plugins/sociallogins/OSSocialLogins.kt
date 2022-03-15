package com.outsystems.plugins.sociallogins

import android.content.Intent
import com.google.gson.Gson
import org.apache.cordova.CallbackContext
import org.apache.cordova.CordovaInterface
import org.apache.cordova.CordovaWebView
import org.json.JSONArray
import org.json.JSONException

class OSSocialLogins : CordovaImplementation() {

    override var callbackContext: CallbackContext? = null

    val gson by lazy { Gson() }

    var socialLoginController: SocialLoginsController? = null

    override fun initialize(cordova: CordovaInterface, webView: CordovaWebView) {
        super.initialize(cordova, webView)

        var socialLoginControllerApple = SocialLoginsAppleController()
        var socialLoginControllerGoogle = SocialLoginsGoogleController(cordova.context)
        socialLoginController = SocialLoginsController(socialLoginControllerApple, socialLoginControllerGoogle)

    }

    override fun execute(
        action: String,
        args: JSONArray,
        callbackContext: CallbackContext
    ): Boolean {
        this.callbackContext = callbackContext

        when (action) {
            "loginApple" -> {
                doLoginApple(args)
            }
            "loginGoogle" -> {
                doLoginGoogle(args)
            }
        }
        return true
    }


    private fun doLoginApple(args : JSONArray) {

        var state = ""
        var clientId = ""
        var redirectUri = ""

        try {
            state = args.get(0).toString()
            clientId = args.get(1).toString()
            redirectUri = args.get(2).toString()

            if(clientId.isNullOrEmpty() || redirectUri.isNullOrEmpty()){
                sendPluginResult(null, Pair(SocialLoginError.MISSING_INPUT_PARAMETERS_ERROR.code, SocialLoginError.MISSING_INPUT_PARAMETERS_ERROR.message))
            }
            else{
                setAsActivityResultCallback()
                socialLoginController?.doLoginApple(state, clientId, redirectUri, cordova.activity)
            }
        }catch (e: JSONException){
            sendPluginResult(null, Pair(SocialLoginError.MISSING_INPUT_PARAMETERS_ERROR.code, SocialLoginError.MISSING_INPUT_PARAMETERS_ERROR.message))
        }catch (e: Exception){
            sendPluginResult(null, Pair(SocialLoginError.APPLE_SIGN_IN_GENERAL_ERROR.code, SocialLoginError.APPLE_SIGN_IN_GENERAL_ERROR.message))
        }

    }

    private fun doLoginGoogle(args : JSONArray) {

        setAsActivityResultCallback()
        socialLoginController?.doLoginGoogle(cordova.activity)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {

        if(resultCode == 0){
            sendPluginResult(null, Pair(SocialLoginError.LOGIN_CANCELLED_ERROR.code, SocialLoginError.LOGIN_CANCELLED_ERROR.message))
        }

        else if(resultCode == 10){
            sendPluginResult(null, Pair(SocialLoginError.APPLE_INVALID_TOKEN_ERROR.code, SocialLoginError.APPLE_INVALID_TOKEN_ERROR.message))
        }

        else if(resultCode == 11){
            sendPluginResult(null, Pair(SocialLoginError.APPLE_SIGN_IN_GENERAL_ERROR.code, SocialLoginError.APPLE_SIGN_IN_GENERAL_ERROR.message))
        }

        else if(resultCode == 1){//Apple Sign in case

            if(intent != null){

                super.onActivityResult(requestCode, resultCode, intent)
                socialLoginController?.handleActivityResult(requestCode, resultCode, intent,
                    {
                        val pluginResponseJson = gson.toJson(it)
                        sendPluginResult(pluginResponseJson, null)
                    },
                    {
                        sendPluginResult(null, Pair(it.code, it.message))
                    }
                )

            }
        }
        else if (requestCode == 2){ //Google sign in case
            super.onActivityResult(requestCode, resultCode, intent)

            if(intent != null){
                try {
                    socialLoginController?.handleActivityResult(requestCode, resultCode, intent,
                        {
                            val pluginResponseJson = gson.toJson(it)
                            sendPluginResult(pluginResponseJson, null)
                        },
                        {
                            sendPluginResult(null, Pair(it.code, it.message))
                        })
                }
                catch(hse : Exception) {
                    sendPluginResult(null, Pair(SocialLoginError.GOOGLE_SIGN_IN_GENERAL_ERROR.code, SocialLoginError.GOOGLE_SIGN_IN_GENERAL_ERROR.message))
                }
            }
        }

    }

    override fun areGooglePlayServicesAvailable(): Boolean {
        //TODO
        return true
    }

    override fun onRequestPermissionResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        //TODO
    }

}