package com.outsystems.plugins.sociallogins

import android.content.Intent
import com.google.gson.Gson
import com.outsystems.plugins.sociallogins.facebook.SocialLoginsFacebookController
import org.apache.cordova.CallbackContext
import org.apache.cordova.CordovaInterface
import org.apache.cordova.CordovaWebView
import org.json.JSONArray
import org.json.JSONException

class OSSocialLogins : CordovaImplementation() {

    override var callbackContext: CallbackContext? = null

    val gson by lazy { Gson() }

    var socialLoginController: SocialLoginsController? = null
    var socialLoginControllerApple = SocialLoginsAppleController()
    var socialloginControlerFacebook = SocialLoginsFacebookController()

    override fun initialize(cordova: CordovaInterface, webView: CordovaWebView) {
        super.initialize(cordova, webView)

        socialLoginController = SocialLoginsController(
            socialLoginControllerApple,
            socialloginControlerFacebook)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        intent?.let {
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