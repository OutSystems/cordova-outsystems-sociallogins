package com.outsystems.plugins.sociallogins

import android.app.Activity
import android.content.Intent
import com.outsystems.plugins.sociallogins.facebook.SocialLoginsFacebookController

class SocialLoginsController(var appleController: SocialLoginsAppleController,
                             var googleController: SocialLoginsGoogleController,
                             var facebookController: SocialLoginsFacebookController) {

    fun doLoginApple(state: String, clientId: String, redirectUri: String, activity: Activity){
        appleController.doLogin(state, clientId, redirectUri, activity)
    }

    fun doLoginGoogle(activity: Activity){
        googleController.doLogin(activity)
    }

    fun doLoginFacebook(activity: Activity) {
        facebookController.doLogin()
    }

    fun handleActivityResult(requestCode: Int,
                             resultCode: Int,
                             intent: Intent,
                             onSuccess : (UserInfo) -> Unit,
                             onError : (SocialLoginError) -> Unit){

        when(resultCode) {
            1 -> {
                //call apple
                appleController.handleActivityResult(requestCode, resultCode, intent,
                    {
                        onSuccess(it)
                    }, {
                        onError(it)
                    }
                )
            }
            2 -> {
                //call google
                googleController.handleActivityResult(requestCode, resultCode, intent,
                    {
                        onSuccess(it)
                    },
                    {
                        onError(it)
                    }
                )
            }
            else -> {

                when(requestCode) {
                    SocialLoginsFacebookController.FACEBOOK_REQUEST_CODE -> {
                        facebookController.handleActivityResult(requestCode, resultCode, intent,
                            {
                                onSuccess(it)
                            },
                            {
                                onError(it)
                            }
                        )

                    }
                }

            }
        }
    }

}