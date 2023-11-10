package com.example.withme

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.withme.PersonalAuthActivity
import com.example.withme.databinding.ActivitySignupBinding
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.withme.AddressActivity
import com.example.withme.R
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class SignupActivity : AppCompatActivity() {
    private val READ_REQUEST_CODE: Int = 42   // 사용자 정의 코드

    private lateinit var retrofit: Retrofit
    private lateinit var service: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val signupBinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(signupBinding.root)
//        setContentView(R.layout.activity_signup)

        // Retrofit 객체 초기화
        retrofit = Retrofit.Builder()
            .baseUrl("http://15.164.94.136:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // 서비스 인스턴스 생성
        service = retrofit.create(ApiService::class.java)

//        // 회원가입 완료 버튼 눌렀을 때
//        signupBinding.btnSignupFin.setOnClickListener {
//            val id = signupBinding.editId.text.toString()
//            val password = signupBinding.editPassword.text.toString()
//
//            if (id.isNotEmpty() && password.isNotEmpty()) {
//                val signupInfo = SignupInfo(id, password)
//
//                // 요청 보내기
//                service.signup(signupInfo).enqueue(object : Callback<signupInfo> {
//                    if (response.isSuccessful) {
//                        // 회원가입 성공 후, 홈 액티비티로 이동
//                         val intent = Intent(this@SignupActivity, HomeFragment::class.java)
//                         startActivity(intent)
//                    } else {
//                        // 서버 응답이 실패했을 때의 동작
//                    }
//                }
//
//                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                        // 요청 자체가 실패했을 때의 동작
//                        Toast.makeText(this@SignupActivity, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            } else {
//                Toast.makeText(this, "아이디 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
//            }
//        }


        // 본인 인증 - 버튼 클릭 시
        val phoneButton: Button = findViewById(R.id.btn_phone)
        phoneButton.setOnClickListener {
            // 다음 액티비티로 이동하는 인텐트 생성
            val intent = Intent(this, PersonalAuthActivity::class.java)
            startActivity(intent)
        }

        // 본인 인증 - 이미지 클릭 시
        val phoneImage: ImageView = findViewById(R.id.img_auth)
        phoneImage.setOnClickListener {
            val intent = Intent(this, PersonalAuthActivity::class.java)
            startActivity(intent)
        }

        // 우편번호, 주소, 상세주소 클릭 시
        val addressButton: EditText = findViewById(R.id.edit_address)
        addressButton.setOnClickListener {
            val intent = Intent(this, AddressActivity::class.java)
            startActivity(intent)
        }
        val addressButton2: EditText = findViewById(R.id.edit_address2)
        addressButton2.setOnClickListener {
            val intent = Intent(this, AddressActivity::class.java)
            startActivity(intent)
        }
        val addressButton3: EditText = findViewById(R.id.edit_address3)
        addressButton3.setOnClickListener {
            val intent = Intent(this, AddressActivity::class.java)
            startActivity(intent)
        }

        // 파일 첨부 클릭 시 -> 파일 첨부 창은 잘 열리는데, 파일 첨부 후에 어떻게 되는지 확인 필요함
        val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val fileName = getFileName(it)   // fileName을 이용하여 업로드 등의 작업 수행
            }
        }
        val fileButton: Button = findViewById(R.id.btn_file_upload)
        fileButton.setOnClickListener {
            getContent.launch("*/*")
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data?.data
            if (uri != null) {
                val fileName = getFileName(uri)
                // fileName을 이용하여 업로드 등의 작업 수행
            }
        }
    }
    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (displayNameIndex != -1) {
                        result = it.getString(displayNameIndex)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != null && cut != -1) {
                result = result?.substring(cut + 1)
            }
        }
        return result ?: ""
    }
}

// 회원가입 정보를 나타내는 데이터 클래스
data class SignupInfo(val id: String, val password: String)

// Retrofit 서비스 인터페이스 정의
interface ApiService {
    @POST("/signup")
    fun signup(@Body signupInfo: SignupInfo): Call<ResponseBody>
}