package com.example.withme

import android.content.Intent
import android.net.http.SslError
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.PermissionRequest
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.withme.databinding.ActivityAddressSearchBinding

class AddressSearchActivity : AppCompatActivity() {
    private lateinit var bind: ActivityAddressSearchBinding

    var type: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityAddressSearchBinding.inflate(layoutInflater)

        val intent = intent
        type = intent.getIntExtra("type", 0)
        val webView = bind.searchWebView
        val bridge = AddressSearchActivity.AndroidBridge(this)
        webView.addJavascriptInterface(bridge, /* name = */ "Android")
        webView.apply {
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.setSupportMultipleWindows(true)
            settings.domStorageEnabled = true
            webViewClient = client
            webChromeClient = chromeClient
        }

        webView.loadUrl("http://10.0.2.2:9001/daum_address.html")

        setContentView(bind.root)
    }

    var client : WebViewClient = object : WebViewClient() {
        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            handler?.proceed();
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return false
        }

    }

    var chromeClient: WebChromeClient = object : WebChromeClient() {
        override fun onPermissionRequest(request: PermissionRequest?) {
            request?.grant(request.resources)
        }
    }

    public fun sendData(data: List<String>)
    {
        val intent = Intent(this, ServiceActivity::class.java)
        val extra : Bundle = Bundle()
        extra.putString("address", data[0])
        extra.putString("name", data[1])
        extra.putInt("type", type)
        intent.putExtras(extra)
        setResult(RESULT_OK, intent)
        finish()
    }

    class AndroidBridge(private val fragment: AddressSearchActivity){
        @JavascriptInterface
        @SuppressWarnings("unused")
        public fun processDATA(roadAdd: String) {
            val parts = roadAdd.split("@")
            fragment.sendData(parts)
        }
    }
}