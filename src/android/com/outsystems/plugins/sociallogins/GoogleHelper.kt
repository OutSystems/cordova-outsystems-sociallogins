package com.outsystems.plugins.sociallogins

import android.content.Context
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GoogleHelper : GoogleHelperInterface {


    override fun getAccessToken(context: Context?, account: GoogleSignInAccount,
                                onSuccess : (String) -> Unit, onError : (SocialLoginError) -> Unit) {

        var accessToken = ""
        val coroutine = CoroutineScope(Dispatchers.IO)

        coroutine.launch {

            if(context != null){
                accessToken = GoogleAuthUtil.getToken(context, account.account, "oauth2:email")

                if(accessToken.isNullOrEmpty()){
                    onError(SocialLoginError.GOOGLE_MISSING_ACCESS_TOKEN_ERROR)
                }
                else{
                    onSuccess(accessToken)
                }
            }
            else{
                onError(SocialLoginError.GOOGLE_SIGN_IN_GENERAL_ERROR)
            }

        }
    }

}