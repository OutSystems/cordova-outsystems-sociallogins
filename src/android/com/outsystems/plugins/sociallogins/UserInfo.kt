package com.outsystems.plugins.sociallogins

data class UserInfo (
    val id: String? = "",
    val email: String? = "",
    val firstName: String? = "",
    val lastName: String? = "",
    val token: String? = "",
    val picture: String? = ""
)