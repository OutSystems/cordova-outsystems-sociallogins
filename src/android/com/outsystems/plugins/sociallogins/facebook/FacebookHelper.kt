package com.outsystems.plugins.sociallogins.facebook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.facebook.CallbackManager
import com.facebook.FacebookSdk
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.login.LoginManager
import com.outsystems.plugins.sociallogins.facebook.dto.FacebookDataResult
import com.outsystems.plugins.sociallogins.facebook.dto.FacebookLoginResult
import org.json.JSONObject

class FacebookHelper(private var activity: Activity? = null): FacebookHelperInterface {
    private val callbackManager: CallbackManager = CallbackManager.Factory.create()
    private val callback = FacebookLoginResultCallback()

    init {
        if(activity != null) {
            FacebookSdk.sdkInitialize(activity!!.applicationContext)

            if (BuildConfig.DEBUG) {
                FacebookSdk.setIsDebugEnabled(true);
                FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
            }
        }
    }

    override fun doLogin(permissions: List<String>) {
        LoginManager.getInstance().registerCallback(callbackManager, callback)
        LoginManager.getInstance().logInWithReadPermissions(activity, permissions)
    }
    override fun getLoginResult(requestCode: Int, resultCode: Int, intent: Intent): FacebookLoginResult {
        callbackManager.onActivityResult(requestCode, resultCode, intent)
        return FacebookLoginResult(callback.status,
            callback.loginResult?.accessToken,
            callback.exception?.message.toString())
    }
    override fun getUserData(fields: String, onCompletion: (FacebookDataResult) -> Unit) {
        callback.loginResult?.accessToken.let { accessToken ->
            val requestCallback = object: GraphRequest.GraphJSONObjectCallback {
                override fun onCompleted(obj: JSONObject?, response: GraphResponse?) {
                    if(response?.error != null) {
                        val errorCode = response.error!!.errorCode
                        val errorMessage = response.error!!.errorMessage
                        onCompletion(
                            FacebookDataResult(
                                isSuccessful = false,
                                errorCode = errorCode,
                                errorMessage = errorMessage ?: "")
                        )
                    }
                    else {
                        onCompletion(
                            FacebookDataResult(
                                isSuccessful = true,
                                resultJson = obj.toString()
                            )
                        )
                    }
                }
            }
            val request = GraphRequest.newMeRequest(accessToken, requestCallback)
            val parameters = Bundle()
            parameters.putString("fields", fields)
            request.parameters = parameters
            request.executeAsync()
        }
    }

}