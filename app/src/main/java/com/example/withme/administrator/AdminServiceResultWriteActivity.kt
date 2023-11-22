package com.example.withme.administrator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.withme.R
import com.example.withme.databinding.ActivityAdminServiceResultWriteBinding
import org.json.JSONException
import org.json.JSONObject

class AdminServiceResultWriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAdminServiceResultWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 생성
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 아이콘 활성화
        actionBar?.setHomeAsUpIndicator(R.drawable.back_button)
        actionBar?.title = "아동 상태 및 병원 동행 결과 작성"

        // 확인 버튼
        val okayButton = binding.okayButton
        okayButton.setOnClickListener {

            val text = binding.editText.text.toString()

            if(text.isNotEmpty()) {

                val url = "http://15.164.94.136:8000/admin/result" // POST 매핑할 URL

                val params = JSONObject()
                params.put("result", text)

                val request = object : JsonObjectRequest (
                    Method.POST, url, params,
                    Response.Listener { response ->
                        // 응답 처리
                        try {
                            // 메인 화면으로 이동
                            val intent = Intent(this, AdminMainActivity::class.java)
                            startActivity(intent)

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    },
                    Response.ErrorListener { error ->
                        // 에러 처리
                        error.printStackTrace()
                        Toast.makeText(this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    }
                ){}

                // 요청 대기열에 요청 추가
                Volley.newRequestQueue(this).add(request)

            } else {
                Toast.makeText(this, "결과를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}