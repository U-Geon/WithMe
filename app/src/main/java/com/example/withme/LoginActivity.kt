package com.example.withme

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.withme.databinding.ActivityLoginBinding
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        loginBinding.signUp.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
        loginBinding.findAccount.setOnClickListener {
            val intent = Intent(this, AccountFindActivity::class.java)
            startActivity(intent)
        }

        loginBinding.loginButton.setOnClickListener {
            val userId = loginBinding.loginId.text.toString()
            val password = loginBinding.loginPassword.text.toString()

            if (userId.isNotEmpty() && password.isNotEmpty()) {
                // 로그인 요청을 보내는 함수 호출
                val url = resources.getString(R.string.ip) + "/login/"

                val params = JSONObject()
                params.put("id", userId)
                params.put("password", password)


                val request = JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    params,
                    Response.Listener { response ->
                        try {
                            /**
                             * 로그인 후 서버에서 json을 전송.
                             * {
                             *     "success": true,
                             *     "isAdmin": true
                             * }
                             */
                            val success = response.getBoolean("success")
                            val isAdmin = response.getBoolean("isAdmin")

                            if (success && !isAdmin) {
                                // 로그인 성공 후 메인 액티비티로!

                                val sharedPreference = getSharedPreferences("other", 0)
                                val editor = sharedPreference.edit()
                                editor.putString("id", userId)
                                editor.apply()

                                val intent = Intent(this, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)


                            } else if(success && isAdmin) {
                                // 관리자 로그인
                                val adminIntent = Intent(this, com.example.withme.administrator.AdminMainActivity::class.java)
                                adminIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                adminIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(adminIntent)
                            } else {
                                // 로그인 실패 처리
                                Toast.makeText(this, "로그인에 실패하였습니다", Toast.LENGTH_SHORT).show()
                            }
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
                Toast.makeText(this, "아이디 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}