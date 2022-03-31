package com.outsystems.plugins.sociallogins.facebook.dto

data class FacebookDataResult(
    val isSuccessful: Boolean,
    val errorCode: Int = 0,
    val errorMessage: String = "",
    val resultJson: String = "")

