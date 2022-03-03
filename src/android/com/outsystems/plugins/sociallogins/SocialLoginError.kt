package com.outsystems.plugins.sociallogins

enum class SocialLoginError(val code: Int, val message: String) {

    NO_USER_LOGGED_IN_ERROR(104, "There is no user logged in"),
    USER_ALREADY_LOGGED_IN_ERROR(105, "User is already logged in"),
    APPLE_SIGN_IN_GENERAL_ERROR(100, "There was an error signing in with Apple"),
    LOGIN_CANCELLED_ERROR(101, "The login was cancelled"),
    APPLE_INVALID_TOKEN_ERROR(103, "Invalid token")

}