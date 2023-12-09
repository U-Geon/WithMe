package com.example.withme

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.withme.databinding.ActivityKidInformationBinding


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
                if (s != null) {
                    if (before > 0 && start > 0 && s[start - 1] == '-') {
                        // BackSpace를 눌렀고, 지워진 문자가 "-"라면
                        val newText = StringBuilder(s).delete(start - 2, start).toString()
                        rrn.removeTextChangedListener(this) // TextWatcher 임시로 제거
                        rrn.setText(newText)
                        rrn.addTextChangedListener(this) // TextWatcher 다시 등록
                        rrn.setSelection(newText.length)
                    } else if (s.length == 6) {
                        val newText = "$s-"
                        rrn.setText(newText)
                        rrn.setSelection(newText.length)
                    } else {
                        rrn.removeTextChangedListener(this) // TextWatcher 임시로 제거
                        rrn.setText(s)
                        rrn.addTextChangedListener(this) // TextWatcher 다시 등록
                        rrn.setSelection(s.length)
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {

            }
        })

        // 다음 버튼
        binding.nextButton.setOnClickListener {
            val kidName = binding.inputName.text.toString()
            val phoneNumber = binding.inputPhoneNumber.text.toString()
            val status = binding.status.text.toString()
            val intent = Intent(this, ServiceActivity::class.java)
            intent.putExtra("kidName", kidName)
            intent.putExtra("phoneNumber", phoneNumber)
            intent.putExtra("rrn", rrn.text.toString())
            intent.putExtra("status", status)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}