package com.outsystems.plugins.sociallogins

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL

// A client to know about WebView navigations
// For API 21 and above
@Suppress("OverridingDeprecatedMember")
class AppleWebViewClient(var context: AppleSignInActivity): WebViewClient() {

    // for API levels < 24
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        handleUrl(Uri.parse(url).toString())
        return true
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val uri = Uri.parse(request?.url.toString())

        //checking if we have a state query parameter in the url
        val isStateNullOrEmpty = uri.getQueryParameter("state").isNullOrEmpty()

        //if we do, then we follow the normal flow
        if(!isStateNullOrEmpty){
            if(request!!.method.equals("GET")){
                handleUrl(request?.url.toString())
            }
        }
        //if we don't, we'll open the url in the devices browser
        else{
            openWebPage(uri)
        }
        return true
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        // retrieve display dimensions
        val displayRectangle = Rect()
        val window = context.window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        // Set height of the Dialog to 90% of the screen
        val layoutParams = view?.layoutParams
        layoutParams?.height = (displayRectangle.height() * 0.9f).toInt()
        view?.layoutParams = layoutParams
    }

    @SuppressLint("LongLogTag")
    private fun handleUrl(url: String) {
        val uri = Uri.parse(url)

        val errorCode = uri.getQueryParameter("errorCode")

        if(errorCode != null && errorCode.isNotEmpty()){
            if(errorCode == "0"){
                context.setResult(0)
                context.finish()
            }
        }

        else{

            val id = uri.getQueryParameter("id")
            val firstName = uri.getQueryParameter("first_name")
            val lastName = uri.getQueryParameter("last_name")
            val token = uri.getQueryParameter("token")
            val email = uri.getQueryParameter("email")
            val state = uri.getQueryParameter("state")

            //send Activity result
            val resultBundle = Bundle()
            resultBundle.putString("id", id)
            resultBundle.putString("firstName", firstName)
            resultBundle.putString("lastName", lastName)
            resultBundle.putString("token", token)
            resultBundle.putString("email", email)

            //get domain from url
            val domain = getDomainName(url)


            //validate token calling backend

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

                    println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")

                    val response = StringBuffer()

                    launch {
                        inputStream.bufferedReader().use {
                            it.lines().forEach { line ->
                                response.append(line)
                            }
                        }

                        val resultIntent = Intent()

                        if(response.toString() == "true"){
                            resultIntent.putExtras(resultBundle)
                            context.setResult(1, resultIntent)
                        }
                        else{
                            context.setResult(10)
                        }
                        context.finish();
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

    private fun openWebPage(webpage: Uri) {
        val browserIntent = Intent(Intent.ACTION_VIEW, webpage)
        startActivity(this.context, browserIntent, null)
    }

}