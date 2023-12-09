package com.example.withme

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

class PersonalAuthActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_auth)

        webView = findViewById(R.id.wv_personalauth)
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true

        webView.webChromeClient = WebChromeClient()

        val htmlData = """
            <html>
                <body onload="requestCert()">
                    <script src="https://cdn.iamport.kr/v1/iamport.js"></script>

                    <script>
                    const userCode = "imp29272276";
                    IMP.init(userCode);

                    function requestCert() {
                        IMP.certification({
                            pg: "inicis_unified",
                            merchant_uid: "test_lohfllvj",
                            name: "",
                            phone: "",
                        });
                    }
                    </script>
                </body>
            </html>
        """.trimIndent()

        webView.loadData(htmlData, "text/html", "UTF-8")
    }
}
