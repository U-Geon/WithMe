package com.akj.withme

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.akj.withme.databinding.ActivityKidInformationBinding


class ㅁㄴKidInformationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityKidInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 생성
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 아이콘 활성화
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
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}