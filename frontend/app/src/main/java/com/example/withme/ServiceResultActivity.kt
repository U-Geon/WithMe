package com.example.withme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.withme.databinding.ActivityServiceResultBinding
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class ServiceResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityServiceResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 생성
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 아이콘 활성화
        actionBar?.setHomeAsUpIndicator(R.drawable.back_button)
        actionBar?.title = "아동 상태 및 병원 동행 결과"

        // 확인 버튼
        val okayButton = binding.okayButton
        okayButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // 아이 정보 받아오기
        val url = resources.getString(R.string.ip) + "/main_result?id=" + intent.getStringExtra("userId").toString()
        Log.d("test", url)
        val resultText = binding.resultText

        val stringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener { response ->
                // 응답 처리
                try {
                    val jsonObject = JSONObject(response)

                    // json value값 받아서 text로 넣기.
                    val result = jsonObject.getString("result")
                    val utf8Result = String(result.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)
                    resultText.text = utf8Result

                } catch (e: JSONException) {
                    resultText.text = "결과가 없습니다."
                }
            },
            Response.ErrorListener { error ->
                // 에러 처리
            }
        ){}

        // 요청 대기열에 요청 추가
        Volley.newRequestQueue(this).add(stringRequest)

    }
}