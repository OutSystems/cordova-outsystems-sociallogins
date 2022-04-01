package com.outsystems.plugins.sociallogins.apple

import com.outsystems.plugins.sociallogins.SocialLoginError

interface AppleHelperInterface {
    fun validateToken(id:String, state:String, redirectURL:String,
                      onSuccess: (Boolean) -> Unit,
                      onError : (SocialLoginError) -> Unit)
}