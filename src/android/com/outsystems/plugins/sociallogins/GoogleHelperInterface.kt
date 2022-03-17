package com.outsystems.plugins.sociallogins

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface GoogleHelperInterface {

    fun getAccessToken(context: Context?, account: GoogleSignInAccount,
                       onSuccess : (String) -> Unit, onError : (SocialLoginError) -> Unit)

}