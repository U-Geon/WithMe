package com.example.withme

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.withme.databinding.ActivityKidInformationBinding
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
            val kidName = binding.inputName.text.toString()
            val phoneNumber = binding.inputPhoneNumber.text.toString()
            val rrn = binding.inputRRN.text.toString()
            val status = binding.status.text.toString()
            val intent = Intent(this, ServiceActivity::class.java)
            intent.putExtra("kidName", kidName)
            intent.putExtra("phoneNumber", phoneNumber)
            intent.putExtra("rrn", rrn)
            intent.putExtra("status", status)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}