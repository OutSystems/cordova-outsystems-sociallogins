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
        if(request!!.method.equals("GET")){
            handleUrl(request?.url.toString())
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

        val id = uri.getQueryParameter("id")
        val firstName = uri.getQueryParameter("first_name")
        val lastName = uri.getQueryParameter("last_name")
        val token = uri.getQueryParameter("token")
        val email = uri.getQueryParameter("email")

        //send Activity result
        val resultBundle = Bundle()
        resultBundle.putString("id", id)
        resultBundle.putString("firstName", firstName)
        resultBundle.putString("lastName", lastName)
        resultBundle.putString("token", token)
        resultBundle.putString("email", email)

        val resultIntent = Intent()
        resultIntent.putExtras(resultBundle)

        context.setResult(1, resultIntent)
        context.finish();
    }
}