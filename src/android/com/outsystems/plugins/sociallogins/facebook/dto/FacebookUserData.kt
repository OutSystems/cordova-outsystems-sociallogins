package com.outsystems.plugins.sociallogins.facebook.dto

import com.google.gson.annotations.SerializedName

data class FacebookUserData(@SerializedName("id")
                       val id: String?,
                       @SerializedName("email")
                       val email: String?,
                       @SerializedName("first_name")
                       val firstName: String?,
                       @SerializedName("last_name")
                       val lastName: String?,
                       @SerializedName("token")
                       var token: String?,
                       @SerializedName("picture")
                       val picture: Picture?) {
    data class Picture(val data: PictureData?)
    data class PictureData(val url: String?)

    constructor(id: String?,
                email: String?,
                firstName: String?,
                lastName: String?,
                token: String?,
                picture: String?) :
            this(id, email, firstName, lastName, token, Picture(PictureData(picture)))

    fun isNotValid(): Boolean {
        return id == null ||
                email == null ||
                firstName == null ||
                lastName == null ||
                token == null ||
                picture?.data?.url == null
    }

}