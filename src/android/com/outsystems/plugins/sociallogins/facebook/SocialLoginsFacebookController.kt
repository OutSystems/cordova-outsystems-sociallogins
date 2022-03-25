package com.outsystems.plugins.sociallogins.facebook

import android.content.Intent
import com.facebook.AccessToken
import com.facebook.login.LoginResult
import com.google.gson.Gson
import com.outsystems.plugins.sociallogins.SocialLoginError
import com.outsystems.plugins.sociallogins.UserInfo
import com.outsystems.plugins.sociallogins.facebook.dto.FacebookLoginResult
import com.outsystems.plugins.sociallogins.facebook.dto.FacebookUserData
import java.lang.Exception

open class SocialLoginsFacebookController(private val facebookHelper: FacebookHelperInterface) {

    private val loginParameters: List<String> = listOf("public_profile", "email")
    private val requestParameters: String = "id,first_name,last_name,picture.width(500).height(500),email"

    fun doLogin(){
        facebookHelper.doLogin(loginParameters)
    }

    fun handleActivityResult(requestCode: Int,
                             resultCode: Int,
                             intent: Intent,
                             onSuccess : (UserInfo) -> Unit,
                             onError : (SocialLoginError) -> Unit) {

        val loginResult = facebookHelper.getLoginResult(requestCode, resultCode, intent)
        when {
            loginResult.status == FacebookLoginResult.CANCEL -> onError(SocialLoginError.FACEBOOK_LOGIN_CANCELLED_ERROR)
            loginResult.status == FacebookLoginResult.NOK -> onError(SocialLoginError.FACEBOOK_SIGN_IN_GENERAL_ERROR)
            loginResult.accessToken?.token.isNullOrEmpty() -> onError(SocialLoginError.FACEBOOK_TOKEN_NOT_FOUND_ERROR)
            else -> getUserData(loginResult.accessToken!!, onSuccess, onError)
        }
    }

    private fun getUserData(accessToken: AccessToken,
                            onSuccess : (UserInfo) -> Unit,
                            onError : (SocialLoginError) -> Unit) {
        facebookHelper.getUserData(requestParameters) { result ->
            try {
                val data = Gson().fromJson(result.resultJson, FacebookUserData::class.java)
                data.token = accessToken.token
                when {
                    !result.isSuccessful -> onError(SocialLoginError.FACEBOOK_USER_DATA_REQUEST_ERROR)
                    result.resultJson.isEmpty() -> onError(SocialLoginError.FACEBOOK_NO_RESULTS_FOUND_ERROR)
                    data.isNotValid() -> onError(SocialLoginError.FACEBOOK_INPUT_PARAMETERS_ERROR)
                    else -> {
                        onSuccess(
                            UserInfo(data.id,
                                data.email,
                                data.firstName,
                                data.lastName,
                                data.token,
                                data.picture!!.data!!.url))
                    }
                }
            } catch (e : Exception) {
                onError(SocialLoginError.FACEBOOK_USER_DATA_REQUEST_ERROR)
            }
        }
    }

    companion object {
        const val FACEBOOK_REQUEST_CODE = 64206
    }

}