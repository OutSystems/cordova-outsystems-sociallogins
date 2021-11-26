package com.outsystems.plugins.sociallogins

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsCallback
import androidx.browser.customtabs.CustomTabsIntent
import com.outsystemsenterprise.enmobile11dev.SocialLoginsPluginDemo.R
import java.util.*

class AppleSignInActivity : Activity() {
    lateinit var redirectUri: String
    lateinit var clientId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        setupAppleWebviewDialog("com.outsystems.mobile.plugin.sociallogin.apple", "https://enmobile11-dev.outsystemsenterprise.com/SL_Core/rest/SocialLoginSignin/AuthRedirectOpenId")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val code = intent?.data?.getQueryParameter("code")
        val firstName = intent?.data?.getQueryParameter("firstName")
    }

    @SuppressLint("SetJavaScriptEnabled", "NewApi")
    fun setupAppleWebviewDialog(clientId: String, redirectUri: String/*, promise: Promise*/) {
        this.clientId = clientId
        this.redirectUri = redirectUri

        //custom chrome tabs solution
        val state = 1333
        val url: String = AppleConstants.AUTHURL + "?response_type=code id_token&response_mode=form_post&client_id=" + clientId + "&scope=" + AppleConstants.SCOPE + "&state=" + state + "&redirect_uri=" + redirectUri

        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))

    }




}