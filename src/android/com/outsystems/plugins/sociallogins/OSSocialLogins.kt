package com.outsystems.plugins.sociallogins

import android.content.Intent
import com.google.gson.Gson
import com.outsystems.plugins.oscordova.CordovaImplementation
import com.outsystems.plugins.sociallogins.SocialLoginsInterface
import com.outsystems.plugins.sociallogins.apple.AppleHelper
import com.outsystems.plugins.sociallogins.apple.SocialLoginsAppleController
import com.outsystems.plugins.sociallogins.facebook.FacebookHelper
import com.outsystems.plugins.sociallogins.facebook.SocialLoginsFacebookController
import com.outsystems.plugins.sociallogins.google.GoogleHelper
import com.outsystems.plugins.sociallogins.google.SocialLoginsGoogleController
import com.outsystems.plugins.sociallogins.linkedin.SocialLoginsLinkedinController
import org.apache.cordova.CallbackContext
import org.apache.cordova.CordovaInterface
import org.apache.cordova.CordovaWebView
import org.json.JSONArray
import org.json.JSONException

class OSSocialLogins : CordovaImplementation() {
    private val gson by lazy { Gson() }
    private var socialLoginController: SocialLoginsController? = null
    override var callbackContext: CallbackContext? = null

    private var appleController: SocialLoginsAppleController? = null
    private var googleController: SocialLoginsGoogleController? = null
    private var linkedinController: SocialLoginsLinkedinController? = null
    private var facebookController: SocialLoginsFacebookController? = null

    private var delegate  = object : SocialLoginsInterface {
        override fun callbackError(error: SocialLoginError) {
            sendPluginResult(null, Pair(error.code.toString(), error.message))
        }
        override fun callbackUserInfo(result: UserInfo) {
            val pluginResponseJson = gson.toJson(result)
            sendPluginResult(pluginResponseJson, null)
        }
    }

    override fun initialize(cordova: CordovaInterface, webView: CordovaWebView) {
        super.initialize(cordova, webView)

        initializeApple()
        initializeGoogle()
        initializeLinkedIn()
        initializeFacebook()

        socialLoginController = SocialLoginsController(
            appleController,
            googleController,
            linkedinController,
            facebookController)
    }

    override fun onRequestPermissionResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        TODO("Not yet implemented")
    }

    private fun initializeApple() {
        if(appleController != null) return
        val appleHelperInterface = AppleHelper()
        appleController = SocialLoginsAppleController(delegate, appleHelperInterface)
    }

    private fun initializeGoogle() {
        val googleHelperInterface = GoogleHelper()
        googleController = SocialLoginsGoogleController(delegate, cordova.context, googleHelperInterface)
    }

    private fun initializeLinkedIn() {
        linkedinController = SocialLoginsLinkedinController(delegate)
    }

    private fun initializeFacebook() {
        val facebookHelper = FacebookHelper(cordova.activity)
        facebookController = SocialLoginsFacebookController(delegate, facebookHelper)
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
                doLoginGoogle()
            }
            "loginFacebook" -> {
                doLoginFacebook()
            }
            "loginLinkedIn" -> {
                doLoginLinkedIn(args)
            }
        }
        return true
    }

    private fun doLoginLinkedIn(args : JSONArray) {
        var state = ""
        var clientId = ""
        var redirectUri = ""

        try {
            state = args.get(0).toString()
            clientId = args.get(1).toString()
            redirectUri = args.get(2).toString()

            if(clientId.isNullOrEmpty() || redirectUri.isNullOrEmpty()){
                sendPluginResult(null, Pair(SocialLoginError.MISSING_INPUT_PARAMETERS_ERROR.code.toString(), SocialLoginError.MISSING_INPUT_PARAMETERS_ERROR.message))
            }
            else{
                setAsActivityResultCallback()
                socialLoginController?.doLoginLinkedin(state, clientId, redirectUri, cordova.activity)
            }
        }catch (e: JSONException){
            sendPluginResult(null, Pair(SocialLoginError.MISSING_INPUT_PARAMETERS_ERROR.code.toString(), SocialLoginError.MISSING_INPUT_PARAMETERS_ERROR.message))
        }catch (e: Exception){
            sendPluginResult(null, Pair(SocialLoginError.APPLE_SIGN_IN_GENERAL_ERROR.code.toString(), SocialLoginError.APPLE_SIGN_IN_GENERAL_ERROR.message))
        }

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
                sendPluginResult(null, Pair(SocialLoginError.MISSING_INPUT_PARAMETERS_ERROR.code.toString(), SocialLoginError.MISSING_INPUT_PARAMETERS_ERROR.message))
            }
            else{
                setAsActivityResultCallback()
                socialLoginController?.doLoginApple(state, clientId, redirectUri, cordova.activity)
            }
        }catch (e: JSONException){
            sendPluginResult(null, Pair(SocialLoginError.MISSING_INPUT_PARAMETERS_ERROR.code.toString(), SocialLoginError.MISSING_INPUT_PARAMETERS_ERROR.message))
        }catch (e: Exception){
            sendPluginResult(null, Pair(SocialLoginError.APPLE_SIGN_IN_GENERAL_ERROR.code.toString(), SocialLoginError.APPLE_SIGN_IN_GENERAL_ERROR.message))
        }

    }
    private fun doLoginGoogle() {
        setAsActivityResultCallback()
        socialLoginController?.doLoginGoogle(cordova.activity)
    }
    private fun doLoginFacebook() {
        setAsActivityResultCallback()
        socialLoginController?.doLoginFacebook()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        socialLoginController?.handleActivityResult(requestCode, resultCode, intent)
    }

    override fun areGooglePlayServicesAvailable(): Boolean {
        return true
    }

}