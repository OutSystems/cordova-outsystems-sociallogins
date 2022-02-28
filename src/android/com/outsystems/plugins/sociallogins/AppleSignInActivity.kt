package com.outsystems.plugins.sociallogins

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsClient

import android.content.ComponentName
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import androidx.browser.customtabs.CustomTabsServiceConnection


class AppleSignInActivity : Activity() {
    lateinit var redirectUri: String
    lateinit var clientId: String
    var state: String = ""

    var myIntent: Intent? = null
    var isFirstTime = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        val bundle = intent.extras
        if (bundle != null) {
            state = bundle.getString("state").toString()
            clientId = bundle.getString("clientId").toString()
            redirectUri = bundle.getString("redirectUri").toString()
        }

        setupAppleWebviewDialog(
            clientId,
            redirectUri)

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        myIntent = intent

        Log.d("onNewIntent:", "passed in onNewIntent")

        val id = intent?.data?.getQueryParameter("id")
        val firstName = intent?.data?.getQueryParameter("first_name")
        val lastName = intent?.data?.getQueryParameter("last_name")
        val token = intent?.data?.getQueryParameter("token")
        val email = intent?.data?.getQueryParameter("email")

        //send Activity result
        val resultBundle = Bundle()
        resultBundle.putString("id", id)
        resultBundle.putString("firstName", firstName)
        resultBundle.putString("lastName", lastName)
        resultBundle.putString("token", token)
        resultBundle.putString("email", email)

        val resultIntent = Intent()
        resultIntent.putExtras(resultBundle)

        setResult(1, resultIntent)
    }

    override fun onResume() {
        super.onResume()
        
        if(!isFirstTime){
            finish()
        }
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
        val url: String = AppleConstants.AUTHURL + "?response_type=code id_token&response_mode=form_post&client_id=" + clientId + "&scope=" + AppleConstants.SCOPE + "&state=" + state + "&redirect_uri=" + redirectUri

        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder
            .setSession(session)
            .build()

        customTabsIntent.launchUrl(this, Uri.parse(url))
        isFirstTime = false
    }




}