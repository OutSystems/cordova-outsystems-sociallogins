package com.outsystems.plugins.sociallogins

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection

class LinkedInSignInActivity : Activity() {

    val SCOPE = "r_liteprofile r_emailaddress"
    val AUTHURL = "https://www.linkedin.com/oauth/v2/authorization"

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

        setupLinkedInWebviewDialog(
            clientId,
            redirectUri)

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val id = intent?.data?.getQueryParameter("id")
        val firstName = intent?.data?.getQueryParameter("first_name")
        val lastName = intent?.data?.getQueryParameter("last_name")
        val token = intent?.data?.getQueryParameter("token")
        val email = intent?.data?.getQueryParameter("email")
        val picture = intent?.data?.getQueryParameter("picture")

        //send Activity result
        val resultBundle = Bundle()
        resultBundle.putString("id", id)
        resultBundle.putString("firstName", firstName)
        resultBundle.putString("lastName", lastName)
        resultBundle.putString("token", token)
        resultBundle.putString("email", email)
        resultBundle.putString("picture", picture)

        val resultIntent = Intent()
        resultIntent.putExtras(resultBundle)

        setResult(3, resultIntent)
    }

    override fun onResume() {
        super.onResume()

        if(!isFirstTime){
            finish()
        }
    }

    @SuppressLint("SetJavaScriptEnabled", "NewApi")
    fun setupLinkedInWebviewDialog(clientId: String, redirectUri: String/*, promise: Promise*/) {
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
        val url: String = this.AUTHURL +
                "?response_type=code&client_id=" + clientId +
                "&scope=" +  SCOPE +
                "&state=" + state +
                "&redirect_uri=" + redirectUri

        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder
            .setSession(session)
            .build()

        customTabsIntent.launchUrl(this, Uri.parse(url))
        isFirstTime = false
    }

}