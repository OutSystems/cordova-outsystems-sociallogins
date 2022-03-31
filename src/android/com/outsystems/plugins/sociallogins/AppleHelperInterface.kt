package com.outsystems.plugins.sociallogins

interface AppleHelperInterface {
    fun validateToken(id:String, state:String, redirectURL:String,
                      onSuccess: (Boolean) -> Unit,
                      onError : (SocialLoginError) -> Unit)
}