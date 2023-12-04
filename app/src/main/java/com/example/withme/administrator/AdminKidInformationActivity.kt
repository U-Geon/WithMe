package com.example.withme.administrator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.withme.R
import com.example.withme.databinding.ActivityAdminKidInformationBinding
import org.json.JSONException
import org.json.JSONObject

class AdminKidInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAdminKidInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 생성
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 아이콘 활성화
        actionBar?.setHomeAsUpIndicator(R.drawable.back_button)
        actionBar?.title = "아이 정보 확인하기"

        /**
         * {
         *      "kidName": xxx,
         *      "phoneNumber": xxx,
         *      "rrn": xxx,
         *      "status": xxx
         * }
         */

        // 아이 정보 받아오기
        val url = resources.getString(R.string.ip) + "/admin_kid_info"

        val phoneNumber = binding.phoneNumber
        val kidName = binding.kidName
        val rrn = binding.rrn
        val status = binding.status

        val stringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener { response ->
                // 응답 처리
                try {
                    val jsonObject = JSONObject(response)

                    // json value값 받아서 text로 넣기.
                    val result1 = jsonObject.getString("kidName")
                    val result2 = jsonObject.getString("phoneNumber")
                    val result3 = jsonObject.getString("rrn")
                    val result4 = jsonObject.getString("status")

                    kidName.text = result1
                    phoneNumber.text = result2
                    rrn.text = result3
                    status.text = result4


                } catch (e: JSONException) {
                    e.printStackTrace()
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