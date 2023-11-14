package com.akj.withme

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.akj.withme.databinding.ActivityAdminServiceBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import org.json.JSONException
import kotlinx.coroutines.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityAdminServiceBinding.inflate(layoutInflater)

        setContentView(bind.root)

        if (!hasPermission()) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            initMapView()
        }

        bind.testButton.setOnClickListener {
            sendCoroutine()
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

    private fun sendCoroutine()
    {
        runBlocking {
            launch {
                while (true) {
                    sendLocation(currentLat, currentLon)
                    delay(1000)
                }
            }
        }
    }

    private fun sendLocation(lat: Double, lon: Double) {
        val url = "http://10.0.2.2:8888/test"
        val params = JSONObject()
        params.put("lat", lat.toString())
        params.put("lan", lon.toString())

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