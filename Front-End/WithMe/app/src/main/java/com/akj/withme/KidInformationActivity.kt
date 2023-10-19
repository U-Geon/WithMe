package com.akj.withme

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akj.withme.databinding.ActivityKidInformationBinding
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject


class KidInformationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityKidInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 생성
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 아이콘 활성화
        actionBar?.setHomeAsUpIndicator(R.drawable.back_button)
        actionBar?.title = "우리 아이 정보"

        // 주민번호 뒷자리 *처리
        val rrn = binding.inputRRN
        rrn.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s != null) {

                    if (s.length == 6) {
                        val newText = "$s-"
                        rrn.setText(newText)
                        rrn.setSelection(newText.length)
                    } else if (s.length > 7) {
                        val originalText = s.toString()
                        val firstPart = originalText.substring(0, 7)
                        val lastPart = originalText.substring(7)

                        val maskedText = firstPart + "*".repeat(lastPart.length)
                        rrn.removeTextChangedListener(this) // TextWatcher 임시로 제거
                        rrn.setText(maskedText)
                        rrn.addTextChangedListener(this) // TextWatcher 다시 등록
                        rrn.setSelection(maskedText.length)
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {

            }
        })

        // 다음 버튼
        binding.nextButton.setOnClickListener {
            // 입력된 정보 POST로 전송
            val url = "http://127.0.0.1:8080/main/status" // 다음 API의 URL로 바꾸기.

            val kidName = binding.inputName.text.toString()
            val phoneNumber = binding.inputPhoneNumber.text.toString()
            val rrn = binding.inputRRN.text.toString()
            val status = binding.status.text.toString()

            val params = JSONObject()
            params.put("kidName", kidName) // 아이 이름
            params.put("phoneNumber", phoneNumber) // 전화번호
            params.put("rrn", rrn) // 주민등록번호
            params.put("status", status) // 아이 상태

            val request = JsonObjectRequest(
                Request.Method.POST,
                url,
                params,
                Response.Listener { response ->
                    try {
                        // 다음 액티비티로 넘어감.
//                        val intent = Intent(this, nextActivity::class.java)
//                        startActivity(intent)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    error.printStackTrace()
                    Toast.makeText(this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            )
            // requestQueue에 저장.
            Volley.newRequestQueue(this).add(request)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}