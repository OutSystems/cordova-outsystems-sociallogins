package com.outsystems.plugins.sociallogins

data class UserInfo (
    val id: String?,
    val email: String?,
    val firstName: String?,
    val lastName: String?,
    var token: String?,
    val picture: String? = ""
)