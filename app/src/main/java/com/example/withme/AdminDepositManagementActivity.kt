package com.example.withme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.withme.databinding.ActivityAdminDepositManagementBinding
import org.json.JSONException
import org.json.JSONObject

class AdminDepositManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDepositManagementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDepositManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 생성
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 아이콘 활성화
        actionBar?.setHomeAsUpIndicator(R.drawable.back_button)
        actionBar?.title = "예치금 관리"

        // id, 사용자 이름, 전화번호, 예치금 받아오는 통신 코드
        val userId = "csw1234"

        if (userId.isNotEmpty()) {
            val url = "http://192.168.80.102:8000/select_name_money_count/"

            val params = JSONObject()
            params.put("id", userId)

            val request = JsonObjectRequest(
                Request.Method.POST,
                url,
                params,
                Response.Listener { response ->
                    try {
                        val id = response.getString("id")
                        binding.tvUserId.text = "$id"

                        val name = response.getString("name")
                        val phonenum = response.getString("phone_number")
                        binding.tvUserNamePhone.text = "($name, $phonenum)"

                        val money = response.getString("money")
                        binding.tvCurrentDeposit.text = "현재 예치금: ${money}원"
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    error.printStackTrace()
                    Toast.makeText(this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            )
            // Volley 요청을 큐에 추가
            Volley.newRequestQueue(this).add(request)
        } else {
            Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
        }

    }
}