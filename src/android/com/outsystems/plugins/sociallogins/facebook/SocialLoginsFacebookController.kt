package com.outsystems.plugins.sociallogins.facebook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.facebook.*

import com.facebook.login.LoginResult

import com.facebook.login.LoginManager
import com.outsystems.plugins.sociallogins.SocialLoginError
import com.outsystems.plugins.sociallogins.UserInfo
import org.json.JSONObject

class SocialLoginsFacebookController {

    private val callbackManager: CallbackManager = CallbackManager.Factory.create()
    private var initedSDK: Boolean = false

    fun doLogin(activity: Activity) {

        if(!initedSDK) {
            FacebookSdk.sdkInitialize(activity.applicationContext)
            initedSDK = true
        }

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {

                    Log.d("FBController","success: ${loginResult.accessToken}")

                    val request = GraphRequest.newMeRequest(loginResult.accessToken,
                        object : GraphRequest.GraphJSONObjectCallback {
                            override fun onCompleted(obj: JSONObject?, response: GraphResponse?) {

                                if(intent.extras == null ||
                                    intent.extras?.getString("id")?.isNotEmpty() == true ||
                                    intent.extras?.getString("email")?.isNotEmpty() == true ||
                                    intent.extras?.getString("token")?.isNotEmpty() == true ) {
                                    onError(SocialLoginError.APPLE_SIGN_IN_GENERAL_ERROR)
                                }


                                Log.d("FB", response.toString())
                            }
                        }
                    )

                    val parameters = Bundle()
                    parameters.putString("fields", "id,first_name,last_name,picture.width(500).height(500),email")
                    request.parameters = parameters
                    request.executeAsync()

                }
                override fun onCancel() {
                    Log.d("FBController","cancel")
                }
                override fun onError(exception: FacebookException) {
                    Log.d("FBController","error: ${exception.message.toString()}")
                }
            })

        LoginManager.getInstance().logInWithReadPermissions(activity, listOf("public_profile", "email"))

    }

    fun handleActivityResult(requestCode: Int,
                             resultCode: Int,
                             intent: Intent,
                             onSuccess : (UserInfo) -> Unit,
                             onError : (SocialLoginError) -> Unit) {
        callbackManager.onActivityResult(requestCode, resultCode, intent)
    }

    companion object {
        const val FACEBOOK_SIGNIN_REQUEST_CODE = 64206
    }
}