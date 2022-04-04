package com.outsystems.plugins.sociallogins.apple

import com.outsystems.plugins.sociallogins.SocialLoginError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL

class AppleHelper: AppleHelperInterface {

    override fun validateToken(id:String, state:String, redirectURL:String, onSuccess: (Boolean) -> Unit, onError : (SocialLoginError) -> Unit) {
        val domain = getDomainName(redirectURL)
        val url = URL("https://$domain/SocialLoginCore/rest/AppleTokenValidation/AppleTokenValidation")

        val coroutine = CoroutineScope(Dispatchers.IO)
        val openedConnection = url.openConnection()

        coroutine.launch {
            with(openedConnection as HttpURLConnection) {
                requestMethod = "POST"
                setRequestProperty("Content-Type", "application/json; utf-8")
                setRequestProperty("Accept", "application/json");
                doOutput = true;

                val json = "{\"code\": \"$id\", \"state\": \"$state\"}"

                outputStream.use { os ->
                    val input: ByteArray = json.toByteArray()
                    os.write(input, 0, input.size)
                }

                val response = StringBuffer()
                launch {
                    inputStream.bufferedReader().use {
                        it.lines().forEach { line ->
                            response.append(line)
                        }
                    }

                    if (response.toString() == "true"){
                        onSuccess(true)
                    } else {
                        onError(SocialLoginError.APPLE_INVALID_TOKEN_ERROR)
                    }

                }
            }
        }
    }

    private fun getDomainName(url: String?): String? {
        val uri = URI(url)
        val domain: String = uri.host
        return if (domain.startsWith("www.")) domain.substring(4) else domain
    }

}



