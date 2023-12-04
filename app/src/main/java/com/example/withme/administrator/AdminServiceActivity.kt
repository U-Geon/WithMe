package com.example.withme.administrator

import android.content.Intent
import com.example.withme.R
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.withme.databinding.ActivityAdminServiceBinding
import com.naver.maps.map.*
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.withme.MainActivity
import org.json.JSONObject
import org.json.JSONException
import kotlin.concurrent.thread

class AdminServiceActivity : AppCompatActivity(), OnMapReadyCallback {
    private val LOCATION_PERMISSION_REQUEST_CODE = 5000
    private val PERMISSIONS = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private lateinit var bind: ActivityAdminServiceBinding
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    private var currentLat: Double = 0.0
    private var currentLon: Double = 0.0
    // adminStatus -1 : 시작 안함 / 0 : 픽업 중 / 1 : 병원 데려다주는 중 / 2 : 집 데려다주는 중 / 3 : 서비스 완료
    private var adminStatus: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityAdminServiceBinding.inflate(layoutInflater)

        setContentView(bind.root)

        if (!hasPermission()) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            initMapView()
        }

        bind.proceedButton.setOnClickListener {
            if(adminStatus == -1) {
                bind.proceedButton.text = "픽업 완료"
                bind.statusText.text= "아이를 데리러 가는 중"
                thread(start = true) {
                    while (true) {
                        sendLocation(currentLat, currentLon, adminStatus)
                        Thread.sleep(2000)

                        if(adminStatus == 3) {
                            val intent = Intent(this, AdminServiceResultWriteActivity::class.java)
                            startActivity(intent)
                            break
                        }
                    }
                }
            } else if(adminStatus == 0) {
                bind.proceedButton.text = "병원 호송 완료"
                bind.statusText.text = "아이를 병원에 데려가는 중"
            } else if(adminStatus == 1) {
                bind.statusText.text = "아이를 귀가시키는 중"
                bind.proceedButton.text = "귀가 완료"
            } else {
                bind.statusText.text = "귀가 완료함"
                // 서비스 종료하기
            }
            adminStatus++
        }
    }

    // ---------------------------------
    // 지도 초기 설정

    private fun initMapView() {
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.mapAdmin) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.mapAdmin, it).commit()
            }

        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    private fun hasPermission(): Boolean {
        for (permission in PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }

        return true
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.uiSettings.isLocationButtonEnabled = true
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        naverMap.addOnLocationChangeListener { location ->
            currentLat = location.latitude
            currentLon = location.longitude
        }

    }

    // ---------------------------------

    // ---------------------------------
    // 서버로 위치 전송

    private fun sendLocation(lat: Double, lon: Double, status: Int) {
        val url = resources.getString(R.string.ip) + "/get_location/"
        val params = JSONObject()
        params.put("lat", lat.toString())
        params.put("lon", lon.toString())
        params.put("status", status);


        val request = JsonObjectRequest(
            Request.Method.POST,
            url,
            params,
            { response -> try {

            } catch(error: JSONException) {
                error.printStackTrace()
            }
            },
            {
                    error -> {
                error.printStackTrace()
            }
            }
        )

        Volley.newRequestQueue(this).add(request)
    }
}