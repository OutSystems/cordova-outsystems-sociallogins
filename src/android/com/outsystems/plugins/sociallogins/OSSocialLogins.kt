package com.outsystems.plugins.sociallogins

import android.app.Activity
import android.content.Intent
import org.apache.cordova.CallbackContext
import org.apache.cordova.CordovaInterface
import org.apache.cordova.CordovaWebView
import org.json.JSONArray

class OSSocialLogins : CordovaImplementation() {

    override var callbackContext: CallbackContext? = null

    var clientId: String = ""
    var redirectUri: String =  ""

    val APPLE_SIGNIN: Int = 13;

    var socialLogin: SocialLogin? = null

    override fun initialize(cordova: CordovaInterface, webView: CordovaWebView) {
        super.initialize(cordova, webView)
        socialLogin = SocialLogin(cordova.activity, cordova.context)
    }

    override fun execute(
        action: String,
        args: JSONArray,
        callbackContext: CallbackContext
    ): Boolean {
        this.callbackContext = callbackContext

        when (action) {
            "coolMethod" -> {
                doLogin(args)
            }
        }
        return true
    }

    private fun doLogin(args : JSONArray) {

        setAsActivityResultCallback()

        this.clientId = "com.outsystems.mobile.plugin.sociallogin.apple"
        this.redirectUri = "https://enmobile11-dev.outsystemsenterprise.com/SL_Core/rest/SocialLoginSignin/AuthRedirectOpenId"

        val provider = "google"

        if(provider == "apple"){
            val currentActivity: Activity? = this.getActivity()
            val intent = Intent(currentActivity, AppleSignInActivity::class.java)

            currentActivity?.startActivityForResult(intent, APPLE_SIGNIN)
        }

        else if (provider == "google"){
            socialLogin?.doLoginGoogle()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
        super.onActivityResult(requestCode, resultCode, intent)

        try {
            socialLogin?.handleActivityResult(requestCode, resultCode, intent)
        }
        catch(hse : Exception) {
            sendPluginResult(null, Pair(1, "errorMessage"))
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