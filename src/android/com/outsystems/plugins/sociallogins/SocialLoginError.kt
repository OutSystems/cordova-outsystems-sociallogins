package com.outsystems.plugins.sociallogins

enum class SocialLoginError(val code: Int, val message: String) {

    APPLE_SIGN_IN_GENERAL_ERROR(100, "There was an error signing in with Apple"),
    LOGIN_CANCELLED_ERROR(101, "The login was cancelled"),
    APPLE_INVALID_TOKEN_ERROR(103, "Invalid token"),
    MISSING_INPUT_PARAMETERS_ERROR(104, "There are input parameters missing"),
    GOOGLE_SIGN_IN_GENERAL_ERROR(105, "There was an error signing in with Google"),
    NO_USER_LOGGED_IN_ERROR(106, "There is no user logged in"),
    USER_ALREADY_LOGGED_IN_ERROR(107, "User is already logged in")


}