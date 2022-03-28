package com.outsystems.plugins.sociallogins

enum class SocialLoginError(val code: Int, val message: String) {
    APPLE_SIGN_IN_GENERAL_ERROR(100, "There was an error signing in with Apple"),
    LOGIN_CANCELLED_ERROR(101, "The login was cancelled"),
    APPLE_INVALID_TOKEN_ERROR(103, "Invalid token"),
    MISSING_INPUT_PARAMETERS_ERROR(104, "There are input parameters missing"),
    APPLE_MISSING_USER_ID(105, "User id missing"),
    APPLE_MISSING_ACCESS_TOKEN_ERROR(106, "Access token missing"),
    APPLE_MISSING_USER_EMAIL(107, "User e-mail missing"),

    GOOGLE_SIGN_IN_GENERAL_ERROR(200, "There was an error signing in with Google"),
    GOOGLE_MISSING_ACCESS_TOKEN_ERROR(202, "Access token missing"),
    GOOGLE_MISSING_USER_ID(203, "User id missing"),

    LINKEDIN_SIGN_IN_GENERAL_ERROR(400, "There was an error signing in with LinkedIn"),
    LINKEDIN_MISSING_ACCESS_TOKEN_ERROR(402, "Access token missing"),
    LINKEDIN_SIGN_IN_MISSING_USER_ID(403, "User id missing"),
    LINKEDIN_SIGN_IN_MISSING_EMAIL(404, "User e-mail missing")
}
