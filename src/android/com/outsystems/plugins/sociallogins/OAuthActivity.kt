package com.outsystems.plugins.sociallogins

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import com.outsystems.plugins.sociallogins.apple.AppleConstants
import com.outsystems.plugins.sociallogins.linkedin.LinkedInConstants

class OAuthActivity : Activity() {

    private companion object {
        const val STABLE_PACKAGE = "com.android.chrome"
    }

    val connection: CustomTabsServiceConnection = object : CustomTabsServiceConnection() {
        override fun onCustomTabsServiceConnected(name: ComponentName, client: CustomTabsClient) {
            openBrowser(client)
        }
        override fun onServiceDisconnected(name: ComponentName) {}
    }

    private lateinit var redirectUri: String
    private lateinit var clientId: String
    private lateinit var state: String
    private lateinit var authUrl : String
    private lateinit var scope : String
    private lateinit var responseType : String
    private var isFirstTime = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras
        if (bundle != null) {
            this.state = bundle.getString("state").toString()
            this.clientId = bundle.getString("clientId").toString()
            this.redirectUri = bundle.getString("redirectUri").toString()
            this.authUrl = bundle.getString("authUrl").toString()
            this.scope = bundle.getString("scope").toString()
            this.responseType = bundle.getString("responseType").toString()
        }

        setupViewDialog()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val id = intent?.data?.getQueryParameter("id")
        val firstName = intent?.data?.getQueryParameter("first_name")
        val lastName = intent?.data?.getQueryParameter("last_name")
        val token = intent?.data?.getQueryParameter("token")
        val email = intent?.data?.getQueryParameter("email")
        val picture = intent?.data?.getQueryParameter("picture")
        val provider = intent?.data?.getQueryParameter("provider")
        val state = intent?.data?.getQueryParameter("state")
        val code = intent?.data?.getQueryParameter("code")
        val errorCode = intent?.data?.getQueryParameter("errorCode")?.toInt()

        val resultBundle = Bundle()
        resultBundle.putString("id", id)
        resultBundle.putString("firstName", firstName)
        resultBundle.putString("lastName", lastName)
        resultBundle.putString("token", token)
        resultBundle.putString("email", email)
        resultBundle.putString("picture", picture)
        resultBundle.putString("state", state)
        resultBundle.putString("code", code)
        errorCode?.let { resultBundle.putInt("errorCode", it) }

        val resultIntent = Intent()
        resultIntent.putExtras(resultBundle)

        if (provider?.toInt() == ProvidersEnum.APPLE.code) {
            setResult(AppleConstants.APPLE_RESULT_CODE, resultIntent)
        } else if (provider?.toInt() == ProvidersEnum.LINKEDIN.code) {
            setResult(LinkedInConstants.LINKEDIN_RESULT_CODE, resultIntent)
        }

    }

    override fun onResume() {
        super.onResume()

        if(!isFirstTime){
            finish()
        }
    }

    @SuppressLint("SetJavaScriptEnabled", "NewApi")
    fun setupViewDialog() {
        val ok = CustomTabsClient.bindCustomTabsService(this, STABLE_PACKAGE , connection)
    }

    fun openBrowser(client : CustomTabsClient) {
        val session = client.newSession(null)!!
        val url: String = this.authUrl + "?response_type=" + this.responseType +
                "&client_id=" + this.clientId +
                "&scope=" +  this.scope +
                "&state=" + this.state +
                "&redirect_uri=" + this.redirectUri

        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder
            .setSession(session)
            .build()

        customTabsIntent.launchUrl(this, Uri.parse(url))
        isFirstTime = false
    }

    override fun onDestroy() {
        super.onDestroy()

        unbindService(connection)
    }

}