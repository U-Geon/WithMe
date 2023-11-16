package com.example.withme

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.withme.databinding.ActivitySignupBinding
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import android.Manifest


class SignupActivity : AppCompatActivity() {
    private val READ_REQUEST_CODE: Int = 42   // 사용자 정의 코드

    private lateinit var retrofit: Retrofit
//    private lateinit var service: ApiService

    private val PERMISSION_Album = 101 // 앨범 권한 처리
    private val PICK_IMAGE = 102 // 이미지 선택 요청 코드
    private val imageView: ImageView by lazy { findViewById(R.id.iv_family) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val signupBinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(signupBinding.root)
//        setContentView(R.layout.activity_signup)

        // 회원가입 완료 버튼 클릭 시, 서버에 정보 보내기
        signupBinding.btnSignupFin.setOnClickListener {
            val userId = signupBinding.editId.text.toString()
            val password = signupBinding.editPassword.text.toString()
            val name = signupBinding.editName.text.toString()
            val phonenum = signupBinding.editPhonenum.text.toString()
            val address = signupBinding.editAddress.text.toString()

            if (userId.isNotEmpty() && password.isNotEmpty()) {
                val url = "http://192.168.80.102:8000/register/"

                val params = JSONObject()
                params.put("id", userId)
                params.put("password", password)
                params.put("name", name)
                params.put("phone_number", phonenum)
                params.put("zip_code", address)

                val request = JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    params,
                    Response.Listener { response ->
                        try {
                            // 회원가입 성공 후 메인 액티비티로
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
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


        // 파일 첨부 - 이미지 창 클릭 시
        val albumImage: ImageView = findViewById(R.id.iv_family)
        albumImage.setOnClickListener {
            requirePermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_Album
            )
        }
        // 파일 첨부 - 버튼 클릭 시
        val albumImage2: ImageView = findViewById(R.id.btn_file_upload)
        albumImage2.setOnClickListener {
            requirePermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_Album
            )
        }

    }


    private fun requirePermissions(permissions: Array<String>, requestCode: Int) {
        val permissionCheck = ContextCompat.checkSelfPermission(this, permissions[0])

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            // 이미 권한이 허용된 경우
            permissionGranted(requestCode)
        } else {
            // 권한이 없는 경우
            ActivityCompat.requestPermissions(this, permissions, requestCode)
        }
    }

    /** 사용자가 권한을 승인하거나 거부한 다음에 호출되는 메서드
     * @param requestCode 요청한 주체를 확인하는 코드
     * @param permissions 요청한 권한 목록
     * @param grantResults 권한 목록에 대한 승인/미승인 값, 권한 목록의 개수와 같은 수의 결괏값이 전달된다.
     * */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            permissionGranted(requestCode)
        } else {
            permissionDenied(requestCode)
        }
    }

    private fun permissionGranted(requestCode: Int) {
        when (requestCode) {
            PERMISSION_Album -> openGallery()
        }
    }

    private fun permissionDenied(requestCode: Int) {
        when (requestCode) {
            PERMISSION_Album -> Toast.makeText(
                this,
                "저장소 권한을 승인해야 앨범에서 이미지를 불러올 수 있습니다.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE -> {
                    data?.data?.let { uri ->
                        imageView.setImageURI(uri)
                    }
                }
            }
        }
    }

}