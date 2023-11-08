package com.example.withme

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.withme.databinding.ActivityAddressBinding
import com.example.withme.databinding.ActivitySignupBinding

class AddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddressBinding
    private lateinit var binding2: ActivitySignupBinding

    inner class MyJavaScriptInterface {
        @JavascriptInterface
        // 주소 검색창에서 주소를 선택하면 그 결과값이 data에 들어옴
        fun processDATA(data: String?) {
            runOnUiThread {
                // EditText에 데이터 설정
                binding2.editAddress.setText(data)
            }
            val intent = Intent()
            intent.putExtra("data", data)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 블로거 페이지의 URL
        val blogspot = "https://billcoreatech.blogspot.com/2022/06/blog-post.html"

        binding.wvAddress.settings.javaScriptEnabled = true
        binding.wvAddress.addJavascriptInterface(MyJavaScriptInterface(), "Android")
        binding.wvAddress.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                // 웹페이지가 로딩되기 전에 웹뷰 숨기기 (블로거 페이지 숨기기 위함)
                binding.wvAddress.visibility = View.INVISIBLE
            }

            override fun onPageFinished(view: WebView, url: String) {
                // 웹페이지 로딩이 완료되면 웹뷰를 다시 표시하고 JavaScript 함수 호출
                binding.wvAddress.visibility = View.VISIBLE
                view.loadUrl("javascript:sample2_execDaumPostcode();")
            }
        }
        binding.wvAddress.loadUrl(blogspot)  // 블로거 페이지 호출
    }
}