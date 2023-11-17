package com.example.withme

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.withme.databinding.ActivitySignupBinding
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import java.io.File





class SignupActivity : AppCompatActivity() {
    private val READ_REQUEST_CODE: Int = 42   // 사용자 정의 코드

    private lateinit var retrofit: Retrofit
//    private lateinit var service: ApiService

    private val PICK_IMAGE = 1001


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
            when {
                ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                    // READ_EXTERNAL_STORAGE의 권한이 PERMISSION_GRANTED와 같다면..
                    //TODO 권한이 잘 부여되었을 때 상황, 갤러리에서 사진을 선택하는 코드 구현
                    navigatePhotos()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    // 권한을 명시적으로 거부한 경우 true
                    // 처음보거나, 다시묻지 않음을 선택한 경우 false
                    //TODO 교육용 팝업 확인 후 권한 팝업을 띄우는 기능
                    showPermissionContextPopup()
                }
                else -> {
                    // 권한을 요청하는 코드를 추가
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        1000)
                }
            }
        }

            // 파일 첨부 - 버튼 클릭 시
//        val albumImage2: ImageView = findViewById(R.id.btn_file_upload)
//        albumImage2.setOnClickListener {
//        when {
//                ContextCompat.checkSelfPermission(this,
//                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
//                    // READ_EXTERNAL_STORAGE의 권한이 PERMISSION_GRANTED와 같다면..
//                    //TODO 권한이 잘 부여되었을 때 상황, 갤러리에서 사진을 선택하는 코드 구현
//                    navigatePhotos()
//                }
//                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
//                    // 권한을 명시적으로 거부한 경우 true
//                    // 처음보거나, 다시묻지 않음을 선택한 경우 false
//                    //TODO 교육용 팝업 확인 후 권한 팝업을 띄우는 기능
//                    showPermissionContextPopup()
//                }
//                else -> {
//                    // 권한을 요청하는 코드를 추가
//                    requestPermissions(
//                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                        1000)
//                }
//            }
//        }

        if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // READ_EXTERNAL_STORAGE의 권한이 PERMISSION_GRANTED와 같다면..
            //TODO 권한이 잘 부여되었을 때상황, 갤러리에서 사진을 선택하는 코드 구현
            navigatePhotos()
        }

        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) run {
            // 권한을 명시적으로 거부한 경우 true
            // 처음보거나, 다시묻지 않음을 선택한 경우 false
            //TODO 교육용 팝업 확인 후 권한 팝업을 띄우는 기능
            showPermissionContextPopup()
        }



    }

    private fun navigatePhotos() {
        // 갤러리에서 사진을 선택하는 코드를 추가
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PICK_IMAGE)
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다")
            .setMessage("전자액자에서 사진을 선택하려면 권한이 필요합니다.")
            .setPositiveButton("동의하기", {_, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
            })
            .setNegativeButton("취소하기",{ _,_ ->})
            .create()
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1000 -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // 권한이 부여 된 것입니다.
                    // 허용 클릭 시
                    navigatePhotos()
                } else {
                    // 거부 클릭시
                    Toast.makeText(this,"권한을 거부했습니다.",Toast.LENGTH_SHORT).show()
                }
            } else -> {
            //Do Nothing
        }
        }
    }

}