package com.outsystems.plugins.sociallogins.facebook

import android.content.Intent
import com.outsystems.plugins.sociallogins.SocialLoginError
import com.outsystems.plugins.sociallogins.UserInfo
import org.json.JSONObject
import com.outsystems.plugins.sociallogins.facebook.FacebookHelperInterface.FacebookLoginResult

class SocialLoginsFacebookController(private val facebookHelper: FacebookHelperInterface) {

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

        if(loginResult.status == FacebookLoginResult.CANCEL) {
            onError(SocialLoginError.LOGIN_CANCELLED_ERROR)
            return
        }

        if(loginResult.status == FacebookLoginResult.NOK) {
            onError(SocialLoginError.FACEBOOK_SIGN_IN_GENERAL_ERROR)
            return
        }

        facebookHelper.getUserData(requestParameters) { result ->
            if(result.isSuccessful && result.result != null) {
                val userInfo = parseFacebookUserData(result.result)
                userInfo.token = result.token
                onSuccess(userInfo)
            }
            else {
                val socialLoginError = SocialLoginError.FACEBOOK_SIGN_IN_GENERAL_ERROR
                onError(socialLoginError)
            }
        }

    }

    private fun parseFacebookUserData(json: JSONObject): UserInfo {
        val id = json.getString("id")
        val email = json.getString("email")
        val firstName = json.getString("first_name")
        val lastName = json.getString("last_name")
        val picture = json.getJSONObject("picture")
            .getJSONObject("data")
            .getString("url")
        return UserInfo(id, email, firstName, lastName, "", picture)
    }

    companion object {
        const val FACEBOOK_RESULT_CODE = 3
        const val FACEBOOK_REQUEST_CODE = 64206
    }

}