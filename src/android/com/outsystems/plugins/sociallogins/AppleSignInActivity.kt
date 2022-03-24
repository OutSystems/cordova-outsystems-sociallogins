package com.outsystems.plugins.sociallogins

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.webkit.WebView

class AppleSignInActivity : Activity() {

    private object AppleConstants {
        const val SCOPE = "email name"
        const val AUTH_URL = "https://appleid.apple.com/auth/authorize"
    }

    private lateinit var redirectUri: String
    private lateinit var clientId: String
    private lateinit var state: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras
        if (bundle != null) {

            if(intent.hasExtra("state") && intent.hasExtra("clientId") && intent.hasExtra("redirectUri")){
                state = bundle.getString("state").toString()
                clientId = bundle.getString("clientId").toString()
                redirectUri = bundle.getString("redirectUri").toString()

                setupAppleWebviewDialog(
                    clientId,
                    redirectUri,
                    state
                )
            }
            else{
                setResult(11)
                finish()
            }
        }
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


        val url: String = AppleConstants.AUTH_URL + "?response_type=code%20id_token&response_mode=form_post&client_id=" + clientId + "&scope=" + AppleConstants.SCOPE + "&state=" + state + "&redirect_uri=" + redirectUri

        webView.loadUrl(url)
        this.setContentView(webView)
    }
    
}