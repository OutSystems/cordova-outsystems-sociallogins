package com.outsystems.plugins.sociallogins

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsClient

import android.content.ComponentName

import androidx.browser.customtabs.CustomTabsServiceConnection




class AppleSignInActivity : Activity() {
    lateinit var redirectUri: String
    lateinit var clientId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        setupAppleWebviewDialog(
            "com.outsystems.mobile.plugin.sociallogin.apple",
            "https://enmobile11-dev.outsystemsenterprise.com/SL_Core/rest/SocialLoginSignin/AuthRedirectOpenId")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val code = intent?.data?.getQueryParameter("Code")
        val user = intent?.data?.getQueryParameter("User")
        val firstName = intent?.data?.getQueryParameter("firstName")
        finish()
    }

    @SuppressLint("SetJavaScriptEnabled", "NewApi")
    fun setupAppleWebviewDialog(clientId: String, redirectUri: String/*, promise: Promise*/) {
        this.clientId = clientId
        this.redirectUri = redirectUri

        val connection: CustomTabsServiceConnection = object : CustomTabsServiceConnection() {

            override fun onCustomTabsServiceConnected(name: ComponentName, client: CustomTabsClient) {
                openBrowser(client)
            }

            override fun onServiceDisconnected(name: ComponentName) {}
        }
        val ok = CustomTabsClient.bindCustomTabsService(this, "com.android.chrome", connection)

    }

    fun openBrowser(client : CustomTabsClient) {

        val session = client.newSession(null)!!

        //custom chrome tabs solution
        val state = 1333
        val url: String = AppleConstants.AUTHURL + "?response_type=code id_token&response_mode=form_post&client_id=" + clientId + "&scope=" + AppleConstants.SCOPE + "&state=" + state + "&redirect_uri=" + redirectUri

        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder
            .setSession(session)
            .build()

        customTabsIntent.launchUrl(this, Uri.parse(url))
    }




}