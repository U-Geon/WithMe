package com.akj.withme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import layout.enableBackButton
import layout.setUpActionBar

class KidInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kid_information)

        setUpActionBar("우리 아이 정보", true)

        // 뒤로가기 버튼 활성화
        enableBackButton()

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}