package com.outsystems.plugins.sociallogins.facebook

import android.content.Intent
import com.outsystems.plugins.sociallogins.facebook.dto.FacebookDataResult
import com.outsystems.plugins.sociallogins.facebook.dto.FacebookLoginResult

interface FacebookHelperInterface {
    fun doLogin(permissions: List<String>)
    fun getLoginResult(requestCode: Int, resultCode: Int, intent: Intent): FacebookLoginResult
    fun getUserData(fields: String, onCompletion: (FacebookDataResult) -> Unit)
}