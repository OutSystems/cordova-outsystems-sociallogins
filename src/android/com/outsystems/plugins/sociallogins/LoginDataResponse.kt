package com.outsystems.plugins.sociallogins

import android.net.Uri

data class LoginDataResponse (
    val id: String,
    val email: String,
    val name: String,
    val photo: String
)