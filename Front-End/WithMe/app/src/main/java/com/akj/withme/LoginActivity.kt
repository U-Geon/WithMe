package com.akj.withme

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akj.withme.databinding.ActivityLoginBinding
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        loginBinding.loginButton.setOnClickListener {
            val userId = loginBinding.loginId.text.toString()
            val password = loginBinding.loginPassword.text.toString()

            if (userId.isNotEmpty() && password.isNotEmpty()) {
                // 로그인 요청을 보내는 함수 호출
                val url = "YOUR_LOGIN_API_URL" // 로그인 API의 URL로 대체해야 합니다.

                val params = JSONObject()
                params.put("username", userId)
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
                             *     "message": "로그인이 성공했습니다."
                             * }
                             */
                            val success = response.getBoolean("success")
                            val message = response.getString("message")

                            if (success) {
                                // 로그인 성공 처리
                                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//                                val intent = Intent(this, MainActivity::class.java)
//                                startActivity(intent)
                            } else {
                                // 로그인 실패 처리
                                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, "아이", Toast.LENGTH_SHORT).show()
            }
        }
    }
}