package com.akj.withme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient

class WebviewActivity : AppCompatActivity() {

    private lateinit var browser: WebView

    inner class MyJavaScriptInterface {
        @JavascriptInterface
        @Suppress("unused")
        fun processDATA(data: String) {
            val extra = Bundle()
            val intent = Intent()
            extra.putString("data", data)
            intent.putExtras(extra)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        browser = findViewById(R.id.wv_)
        browser.settings.javaScriptEnabled = true
        browser.addJavascriptInterface(MyJavaScriptInterface(), "Android")

        browser.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                browser.loadUrl("javascript:sample2_execDaumPostcode();")
            }
        }

        browser.loadUrl("http://서버주소/daum.html")
    }
}