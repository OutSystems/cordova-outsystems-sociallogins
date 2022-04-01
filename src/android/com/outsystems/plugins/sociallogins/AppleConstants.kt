package com.outsystems.plugins.sociallogins.apple

object AppleConstants {
    const val SCOPE = "email%20name"
    const val AUTH_URL = "https://appleid.apple.com/auth/authorize"
    const val RESPONSE_TYPE = "code%20id_token" + "&response_mode=form_post"
    const val APPLE_REQUEST_CODE = 1;
    const val APPLE_RESULT_CODE = 1;
}