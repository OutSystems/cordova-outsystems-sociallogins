package com.outsystems.plugins.sociallogins.facebook

import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.outsystems.plugins.sociallogins.facebook.dto.FacebookLoginResult

class FacebookLoginResultCallback: FacebookCallback<LoginResult> {

    var status: Int = FacebookLoginResult.NOK
    var loginResult: LoginResult? = null
    var exception: FacebookException? = null

    override fun onSuccess(lr: LoginResult) {
        loginResult = lr
        status = FacebookLoginResult.OK
    }
    override fun onCancel() {
        status = FacebookLoginResult.CANCEL
    }
    override fun onError(fe: FacebookException) {
        exception = fe
        status = FacebookLoginResult.NOK
    }
}