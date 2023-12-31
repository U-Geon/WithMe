package com.example.withme.administrator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.withme.R
import com.example.withme.databinding.ActivityAdminDepositManagementBinding
import org.json.JSONException
import org.json.JSONObject

class AdminDepositManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDepositManagementBinding
    private var money: Int = 0
    private var originMoney: Int = 0

    private fun sendMoneyUpdateRequest(updatedMoney: Int) {
        val sharedPreference = getSharedPreferences("admin", 0)  // SharedPreferences 인스턴스
        val userId = sharedPreference.getString("admin_deposit_id", "")

        val url = resources.getString(R.string.ip) + "/modify_deposit/"

        val params = JSONObject()
        params.put("id", userId)
        params.put("amount", updatedMoney)

        val request = JsonObjectRequest(
            Request.Method.POST,
            url,
            params,
            Response.Listener { response ->
                print("연결 성공")
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        )

        // Volley 요청을 큐에 추가
        Volley.newRequestQueue(this).add(request)
    }

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
        val sharedPreference = getSharedPreferences("admin", 0)  // SharedPreferences 인스턴스
        val userId = sharedPreference.getString("admin_deposit_id", "")

        val url = resources.getString(R.string.ip) + "/select_name_money_count_phone_number/"

        val params = JSONObject()
        params.put("id", userId)

        val request = JsonObjectRequest(
            Request.Method.POST,
            url,
            params,
            Response.Listener { response ->
                try {
                    binding.tvUserId.text = "$userId"

                    val name = response.getString("name")
                    val phonenum = response.getString("phone_number")
                    binding.tvUserNamePhone.text = "($name, $phonenum)"

                    originMoney = response.getInt("money")

                    binding.tvCurrentDeposit.text = "현재 예치금: ${originMoney}원"
                    binding.tvExpectationDeposit.text = "최종 예치금: ${originMoney}원"
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
        
        // 예치금 플러스 관련
        val plusDepositEdit: EditText = findViewById(R.id.et_plus_deposit)
        val plusDepositButton: Button = findViewById(R.id.btn_plus_deposit)
        plusDepositButton.setOnClickListener {
            val depositAmountText: String = plusDepositEdit.text.toString()
            // 입력된 숫자가 비어있지 않다면 더하도록
            if (depositAmountText.isNotEmpty()) {
                val depositAmount: Int = depositAmountText.toInt()
                money = originMoney + depositAmount
                originMoney = money
                binding.tvExpectationDeposit.text = "최종 예치금: ${money}원"
                plusDepositEdit.text = Editable.Factory.getInstance().newEditable("")  // 입력창 비우기
            } else {
                Toast.makeText(this@AdminDepositManagementActivity, "값을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 예치금 마이너스 관련
        val minusDepositEdit: EditText = findViewById(R.id.et_minus_deposit)
        val minusDepositButton: Button = findViewById(R.id.btn_minus_deposit)
        minusDepositButton.setOnClickListener {
            val depositAmountText: String = minusDepositEdit.text.toString()
            // 입력된 숫자가 비어있지 않고, 현재 예치금에서 입력된 숫자를 뺐을 때 0 이상일 때만 빼도록
            if (depositAmountText.isNotEmpty()) {
                if (originMoney - depositAmountText.toInt() >= 0) {
                    val depositAmount: Int = depositAmountText.toInt()
                    money = originMoney - depositAmount
                    originMoney = money
                    binding.tvExpectationDeposit.text = "최종 예치금: ${money}원"
                    minusDepositEdit.text = Editable.Factory.getInstance().newEditable("")
                } else {
                    Toast.makeText(this@AdminDepositManagementActivity, "현재 예치금보다 큰 값이기 때문에 뺄 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@AdminDepositManagementActivity, "값을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 수정완료 버튼 누르면 서버에 바뀐 예치금 전송
        val signupFinButton: Button = findViewById(R.id.btn_signup_fin)
        signupFinButton.setOnClickListener {
            // 서버에 moneyInt 값을 업데이트
            val amount = money - originMoney
            sendMoneyUpdateRequest(amount)
            binding.etPlusDeposit.text = null
            binding.etMinusDeposit.text = null
            Toast.makeText(this, "최종 예치금 ${money}로 수정 완료", Toast.LENGTH_SHORT).show()
        }
    }
}