package com.outsystems.plugins.sociallogins

enum class SocialLoginError(val code: Int, val message: String) {

    NO_USER_LOGGED_IN_ERROR(100, "There is no user logged in"),
    USER_ALREADY_LOGGED_IN_ERROR(101, "User is already logged in")

}