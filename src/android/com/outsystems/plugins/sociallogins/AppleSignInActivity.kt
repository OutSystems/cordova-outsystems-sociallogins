package com.outsystems.plugins.sociallogins

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.webkit.WebView

class AppleSignInActivity : Activity() {
    lateinit var redirectUri: String
    lateinit var clientId: String
    var state: String = ""
    var isFirstTime = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras
        if (bundle != null) {
            state = bundle.getString("state").toString()
            clientId = bundle.getString("clientId").toString()
            redirectUri = bundle.getString("redirectUri").toString()
        }

        setupAppleWebviewDialog(
            clientId,
            redirectUri,
            state)
    }
    
    @SuppressLint("SetJavaScriptEnabled", "NewApi")
    fun setupAppleWebviewDialog(clientId: String, redirectUri: String, state: String) {

        //web view solution
        val webView = WebView(this).apply {
            settings.apply {
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
            }
        }
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.webViewClient = AppleWebViewClient(this)


        val url: String = AppleConstants.AUTHURL + "?response_type=code id_token&response_mode=form_post&client_id=" + clientId + "&scope=" + AppleConstants.SCOPE + "&state=" + state + "&redirect_uri=" + redirectUri

        webView.loadUrl(url)
        this.setContentView(webView)
    }
    
}