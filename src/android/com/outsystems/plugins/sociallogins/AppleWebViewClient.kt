package com.outsystems.plugins.sociallogins

import android.annotation.SuppressLint
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi

// A client to know about WebView navigations
// For API 21 and above
@Suppress("OverridingDeprecatedMember")
 class AppleWebViewClient(var context: AppleSignInActivity/*, var promise: Promise*/): WebViewClient() {

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
        val layoutparms = view?.layoutParams
        layoutparms?.height = (displayRectangle.height() * 0.9f).toInt()
        view?.layoutParams = layoutparms
    }

    @SuppressLint("LongLogTag")
    private fun handleUrl(url: String) {
        val uri = Uri.parse(url)

        //if (!url.contains("authorizationCode")) {
        //    return
        //}

        val authCode: String = uri.getQueryParameter("authorizationCode") ?: ""

        if (authCode == "") {
            val message = "We couldn't get the Auth Code"
            Log.e("Error",message )
            //promise.reject("", message)
        }

        val appleData = AppleData(authCode)

        appleData.firstName = uri.getQueryParameter("firstName") ?: ""
        appleData.lastName = uri.getQueryParameter("lastName") ?: ""

        //promise.resolve(appleData.toMap());
        context.finish();
    }
}