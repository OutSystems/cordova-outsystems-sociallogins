package com.outsystems.plugins.sociallogins

class AppleData(val authorizationCode: String) {


    var firstName: String? = ""

    var lastName: String? = ""

    fun toMap(): MutableMap<String, String> {
        //val map: WritableMap = Arguments.createMap()
        val map: MutableMap<String, String> = mutableMapOf()
        if (map != null) {
            map.put("authorizationCode", authorizationCode)
            firstName?.let { map.put("firstName", it) }
            lastName?.let { map.put("lastName", it) }
        }
        return  map;
    }
}