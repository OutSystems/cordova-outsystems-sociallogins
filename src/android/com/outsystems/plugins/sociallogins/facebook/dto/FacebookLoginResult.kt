package com.outsystems.plugins.sociallogins.facebook.dto

import com.facebook.AccessToken

data class FacebookLoginResult(val status: Int,
                               val accessToken: AccessToken? = null,
                               val message: String? = null) {
    companion object {
        const val OK: Int = 0
        const val NOK: Int = 1
        const val CANCEL: Int = 2
    }
}
