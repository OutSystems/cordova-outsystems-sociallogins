package com.outsystems.plugins.sociallogins

enum class SocialLoginError(val code: Int, val message: String) {

    APPLE_SIGN_IN_GENERAL_ERROR(100, "There was an error signing in with Apple"),
    LOGIN_CANCELLED_ERROR(101, "The login was cancelled"),
    APPLE_INVALID_TOKEN_ERROR(103, "Invalid token"),
    MISSING_INPUT_PARAMETERS_ERROR(104, "There are input parameters missing"),

    GOOGLE_SIGN_IN_GENERAL_ERROR(200, "There was an error signing in with Google"),
    GOOGLE_MISSING_ACCESS_TOKEN_ERROR(202, "Access token missing"),
    GOOGLE_MISSING_USER_ID(203, "User id missing"),

    FACEBOOK_SIGN_IN_GENERAL_ERROR(300, "There was an error signing in with Facebook"),
    FACEBOOK_LOGIN_CANCELLED_ERROR(300, "Sign in with Facebook was cancelled."),
    FACEBOOK_TOKEN_NOT_FOUND_ERROR(302, "Some error occurred while signing in with Facebook as no token was found."),
    FACEBOOK_NO_RESULTS_FOUND_ERROR(303, "No results were returned while signing in with Facebook."),
    FACEBOOK_INPUT_PARAMETERS_ERROR(304, "Couldn't fetch information for all parameters requested."),
    FACEBOOK_USER_DATA_REQUEST_ERROR(305, "There was a problem while request the user's data on Facebook."),
    FACEBOOK_CONFIGURATION_NOT_VALID(399, "The configurations for Facebook were not properly set.")

}