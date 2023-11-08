package com.example.withme

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.withme.R
import com.example.withme.databinding.ActivityPaymentBinding
import java.net.URISyntaxException

class PaymentActivity : AppCompatActivity() {
    companion object {
        const val MERCHANT_URL = "https://web.nicepay.co.kr/demo/v3/mobileReq.jsp"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        // 툴바 생성
//        val toolbar = binding.toolbar
//        setSupportActionBar(toolbar)
//
//        val actionBar = supportActionBar
//        actionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 아이콘 활성화
//        actionBar?.setHomeAsUpIndicator(R.drawable.back_button)
//        actionBar?.title = "예치금 입금하기"

        // 결제 부분
        binding.wvPayment.webViewClient = WebViewClientClass()

        val settings = binding.wvPayment.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true

        binding.wvPayment.setWebChromeClient(object: WebChromeClient() {
            //alert
        })

        settings.cacheMode = WebSettings.LOAD_DEFAULT

        if( android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {    //Android 5.0 이상
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW)
            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(binding.wvPayment, true)
        } else {
            CookieManager.getInstance().setAcceptCookie(true)
        }

        binding.wvPayment.loadUrl(MERCHANT_URL)
    }

    private class WebViewClientClass : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            println("url : " + url)

            try {
                if( url != null && (url.startsWith("intent:")
                            || url.contains("market://")
                            || url.contains("vguard")
                            || url.contains("droidxantivirus")
                            || url.contains("v3mobile")
                            || url.contains(".apk")
                            || url.contains("mvaccine")
                            || url.contains("smartwall://")
                            || url.contains("nidlogin://")
                            || url.contains("http://m.ahnlab.com/kr/site/download")) ) {

                    var intent: Intent? = null

                    try {
                        intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                    } catch (e: URISyntaxException) {
                        println("error : " + e.printStackTrace())
                    }

                    if( view?.context?.packageManager?.resolveActivity(intent!!, 0) == null ) {
                        val pkgName = intent?.`package`
                        if( pkgName != null ) {
                            val uri = Uri.parse("market://search?q=pname:" + pkgName)
                            intent = Intent(Intent.ACTION_VIEW, uri)
                            view?.context?.startActivity(intent)
                        }
                    } else {
                        val uri = Uri.parse(intent?.dataString)
                        intent = Intent(Intent.ACTION_VIEW, uri)
                        view?.context?.startActivity(intent)
                    }
                } else {
                    if (url != null) {
                        view?.loadUrl(url)
                    }
                }
            } catch (e: Exception) {
                println("error : " + e.printStackTrace())
                return false
            }

            return true
        }
    }
}