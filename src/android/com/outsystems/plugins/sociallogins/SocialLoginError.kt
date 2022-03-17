package com.outsystems.plugins.sociallogins

enum class SocialLoginError(val code: Int, val message: String) {

    APPLE_SIGN_IN_GENERAL_ERROR(100, "There was an error signing in with Apple"),
    LOGIN_CANCELLED_ERROR(101, "The login was cancelled"),
    APPLE_INVALID_TOKEN_ERROR(103, "Invalid token"),
    MISSING_INPUT_PARAMETERS_ERROR(104, "There are input parameters missing"),
    GOOGLE_SIGN_IN_GENERAL_ERROR(200, "There was an error signing in with Google"),
    GOOGLE_MISSING_ACCESS_TOKEN_ERROR(202, "Access token missing"),
    GOOGLE_MISSING_USER_ID(203, "User id missing")

}