package com.outsystems.plugins.sociallogins

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import org.apache.cordova.CallbackContext
import org.apache.cordova.CordovaInterface
import org.apache.cordova.CordovaWebView
import org.json.JSONArray
import com.google.gson.Gson

class OSSocialLogins : CordovaImplementation() {

    override var callbackContext: CallbackContext? = null

    val APPLE_SIGNIN: Int = 13;

    //var socialLogin: SocialLoginsGoogleController? = null

    //var socialLoginController: SocialLoginsController? = null

    val gson by lazy { Gson() }

    override fun initialize(cordova: CordovaInterface, webView: CordovaWebView) {
        super.initialize(cordova, webView)

        //socialLogin = SocialLoginsGoogleController(cordova.activity, cordova.context)

        //socialLoginController = SocialLoginsController(cordova.activity, cordova.context)

    }

    override fun execute(
        action: String,
        args: JSONArray,
        callbackContext: CallbackContext
    ): Boolean {
        this.callbackContext = callbackContext

        when (action) {
            "login" -> {
                doLoginApple(args)
            }
            "loginApple" -> {
                doLoginApple(args)
            }
            "logout" -> {
                //doLogout(args)
            }
            "getLoginData" -> {
                //getLoginData(args)
            }
            "checkLoginStatus" -> {
                //checkLoginStatus(args)
            }
        }
        return true
    }

    private fun doLoginGoogle(args : JSONArray){
        //TODO
        /*
        if(!socialLogin?.isUserLoggedIn()!!.first){
            setAsActivityResultCallback()
            socialLogin?.doLoginGoogle()
        }
        //decide what we should do when user is already logged in
        //maybe send back a message saying login already done?
         */
    }

    private fun doLoginApple(args : JSONArray) {

        val state = args.get(0).toString()
        val clientId = args.get(1).toString()
        val redirectUri = args.get(2).toString()


        val currentActivity: Activity? = this.getActivity()


        //socialLoginController?.doLoginApple(state, clientId, redirectUri)


        val intent = Intent(currentActivity, AppleSignInActivity::class.java)

        val bundle = Bundle()
        bundle.putString("state", state)
        bundle.putString("clientId", clientId)
        bundle.putString("redirectUri", redirectUri)

        intent.putExtras(bundle)

        currentActivity?.startActivityForResult(intent, APPLE_SIGNIN)

        setAsActivityResultCallback()
    }
    
    /*

    private fun doLogout(args: JSONArray) {
        val provider = "google"

        if(provider == "google"){
            socialLogin?.doLogoutGoogle()
        }
    }

    private fun getLoginData(args: JSONArray) {

        socialLogin?.getLoginData(
            {
                response ->
                    val pluginResponseJson = gson.toJson(response)
                    sendPluginResult(pluginResponseJson, null)
            },
            {
                error ->
                    sendPluginResult(null, Pair(error.code, error.message))
            })
    }

    private fun checkLoginStatus(args: JSONArray) {
        if(socialLogin?.isUserLoggedIn()!!.first){
            sendPluginResult("logged", null)
        }
        else{
            sendPluginResult("notLogged", null)
        }
    }
    
     */

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
        super.onActivityResult(requestCode, resultCode, intent)

        if(resultCode == 1){//Apple Sign in case
            val returnBundle = intent.extras

            if (returnBundle != null) {

                val userInfo = UserInfo(
                    returnBundle.getString("id"),
                    returnBundle.getString("email"),
                    returnBundle.getString("firstName"),
                    returnBundle.getString("lastName"),
                    returnBundle.getString("token")
                )
                val pluginResponseJson = gson.toJson(userInfo)
                sendPluginResult(pluginResponseJson, null)

            }

        }
        else{ // for now, its only google sign in case
            /*
            try {
                socialLogin?.handleActivityResult(requestCode, resultCode, intent)
                //implement in closure to sendPluginResult after handleActivityResult returns success
                sendPluginResult("success", null)
            }
            catch(hse : Exception) {
                sendPluginResult(null, Pair(1, "errorMessage"))
            }
             */
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