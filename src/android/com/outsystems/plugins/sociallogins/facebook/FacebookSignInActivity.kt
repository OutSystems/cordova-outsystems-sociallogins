package com.outsystems.plugins.sociallogins.facebook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import org.json.JSONObject

class FacebookSignInActivity: Activity() {

    private val callbackManager: CallbackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.d("FB","success: ${loginResult.accessToken}")

                    val request = GraphRequest.newMeRequest(loginResult.accessToken,
                        object : GraphRequest.GraphJSONObjectCallback {
                            override fun onCompleted(obj: JSONObject?, response: GraphResponse?) {
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
                    Log.d("FB","cancel")
                }
                override fun onError(exception: FacebookException) {
                    Log.d("FB","error: ${exception.message.toString()}")
                }
            }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}