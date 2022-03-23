package com.outsystems.plugins.sociallogins.facebook

import android.content.Intent
import android.os.Bundle
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import org.json.JSONObject

interface FacebookHelperInterface {
    fun doLogin(permissions: List<String>)
    fun getLoginResult(requestCode: Int, resultCode: Int, intent: Intent): FacebookLoginResult
    fun getUserData(fields: String, onResult: (FacebookUserData) -> Unit)

    data class FacebookLoginResult(val status: Int, val message: String? = null) {
        companion object {
            const val OK: Int = 0
            const val NOK: Int = 1
            const val CANCEL: Int = 2
        }
    }
    data class FacebookUserData(val isSuccessful: Boolean,
                                val result: JSONObject? = null,
                                var token: String = "",
                                val errorCode: Int = 0,
                                val errorMessage: String = "")
}