package com.outsystems.plugins.sociallogins.facebook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.outsystems.plugins.sociallogins.facebook.FacebookHelperInterface.FacebookUserData
import com.outsystems.plugins.sociallogins.facebook.FacebookHelperInterface.FacebookLoginResult

class FacebookHelper(private var activity: Activity): FacebookHelperInterface {

    private val callbackManager: CallbackManager = CallbackManager.Factory.create()
    private val callback = object : FacebookCallback<LoginResult> {
        override fun onSuccess(lr: LoginResult) {
            loginResult = lr
        }
        override fun onCancel() {
            Log.d("FB","cancel")
        }
        override fun onError(fe: FacebookException) {
            exception = fe
        }
    }
    private var loginResult: LoginResult? = null
    private var exception: FacebookException? = null

    override fun doLogin(permissions: List<String>) {
        LoginManager.getInstance().registerCallback(callbackManager, callback)
        LoginManager.getInstance().logInWithReadPermissions(activity, permissions)
    }
    override fun getLoginResult(requestCode: Int, resultCode: Int, intent: Intent): FacebookLoginResult {
        callbackManager.onActivityResult(requestCode, resultCode, intent)

        exception?.let { e ->
            return FacebookLoginResult(FacebookLoginResult.NOK, e.message.toString())
        }

        loginResult?.let {
            return FacebookLoginResult(FacebookLoginResult.OK)
        }

        return FacebookLoginResult(FacebookLoginResult.CANCEL)
    }
    override fun getUserData(fields: String, onResult: (FacebookUserData) -> Unit) {
        loginResult?.accessToken.let { accessToken ->
            val request = GraphRequest.newMeRequest(accessToken)
            { obj, response ->
                if (response?.error != null) {
                    val errorCode = response.error!!.errorCode
                    val errorMessage = response.error!!.errorMessage
                    onResult(
                        FacebookUserData(
                            isSuccessful = false,
                            errorCode = errorCode,
                            errorMessage = errorMessage ?: ""
                        )
                    )
                } else {
                    onResult(
                        FacebookUserData(
                            isSuccessful = true,
                            result = obj,
                            token = accessToken?.token ?: ""
                        )
                    )
                }
            }
            val parameters = Bundle()
            parameters.putString("fields", fields)
            request.parameters = parameters
            request.executeAsync()
        }
    }

}