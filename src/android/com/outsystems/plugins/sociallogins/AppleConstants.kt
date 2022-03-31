package com.outsystems.plugins.sociallogins

object AppleConstants {
    const val SCOPE = "email name"
    const val AUTH_URL = "https://appleid.apple.com/auth/authorize"
    const val RESPONSE_TYPE = "code%20id_token" + "&response_mode=form_post"
    const val APPLE_SIGN_IN_REQUEST_CODE = 1;
    const val APPLE_SIGN_IN_RESULT_CODE = 1;
}