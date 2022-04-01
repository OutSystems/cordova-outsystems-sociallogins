package com.outsystems.plugins.sociallogins.google

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.outsystems.plugins.sociallogins.SocialLoginError

interface GoogleHelperInterface {

    fun getAccessToken(context: Context?, account: GoogleSignInAccount,
                       onSuccess : (String) -> Unit, onError : (SocialLoginError) -> Unit)

}