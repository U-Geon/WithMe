package com.example.withme

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.withme.databinding.ActivityServiceBinding
import android.widget.Toast
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.util.Log
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.util.FusedLocationSource
import androidx.fragment.app.FragmentActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.JsonObjectRequest
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import org.json.JSONException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.Locale
import kotlin.concurrent.thread
import org.json.JSONObject

class ServiceActivity : AppCompatActivity(), OnMapReadyCallback {
    private val LOCATION_PERMISSION_REQUEST_CODE = 5000
    private val PERMISSIONS = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private lateinit var bind: ActivityServiceBinding
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityServiceBinding.inflate(layoutInflater)
        setContentView(bind.root)

        if (!hasPermission()) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            initMapView()
        }

        bind.submitButton.setOnClickListener {
            if(bind.startInput.text.isEmpty() or bind.hospitalInput.text.isEmpty() or bind.endInput.text.isEmpty())
                Toast.makeText(this, "모든 장소를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            else {
                val url = "http://15.164.94.136:8000/"
                val params = JSONObject()
                params.put("startPosition", bind.startInput.text.toString())
                params.put("hospitalPosition", bind.hospitalInput.text.toString())
                params.put("endPosition", bind.endInput.text.toString())

                val request = JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    params,
                    {
                        response -> try {
                            Log.d("TEST", "서버에 제출 성공")
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
            }
        }

        bind.startButton.setOnClickListener {
            if(bind.startInput.text.isEmpty()) {
                Toast.makeText(this, "장소를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                var coordinates : Array<String> = emptyArray()
                val thr = thread(start = true) {
                    coordinates = search(bind.startInput.text.toString())
                    for(elem in coordinates)
                        Log.d("TEST", elem)
                }

                thr.join()

                if(!coordinates.isEmpty())
                    setMarker(coordinates)
                else
                    Log.d("TEST", "비어있음")
            }
        }

        bind.hospitalButton.setOnClickListener {
            if(bind.hospitalInput.text.isEmpty()) {
                Toast.makeText(this, "장소를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                var coordinates : Array<String> = emptyArray()
                val thr = thread(start = true) {
                    coordinates = search(bind.hospitalInput.text.toString())
                    for(elem in coordinates)
                        Log.d("TEST", elem)
                }

                thr.join()

                if(!coordinates.isEmpty())
                    setMarker(coordinates)
                else
                    Log.d("TEST", "비어있음")
            }
        }

        bind.endButton.setOnClickListener {
            if(bind.endInput.text.isEmpty()) {
                Toast.makeText(this, "장소를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                var coordinates : Array<String> = emptyArray()
                val thr = thread(start = true) {
                    coordinates = search(bind.endInput.text.toString())
                    for(elem in coordinates)
                        Log.d("TEST", elem)
                }

                thr.join()

                if(!coordinates.isEmpty())
                    setMarker(coordinates)
                else
                    Log.d("TEST", "비어있음")
            }
        }
    }


    // -------------------------------------------------
    // 주소 검색

    private fun search(target: String): Array<String>
    {
        try {
            var bufferedReader : BufferedReader
            val stringBuilder : StringBuilder = StringBuilder()
            val query = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + URLEncoder.encode(target, "UTF-8")
            val url = URL(query)
            val conn = url.openConnection() as HttpURLConnection

            if(conn != null) {
                conn.connectTimeout = 5000
                conn.readTimeout = 5000
                conn.requestMethod = "GET"
                conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", "u51ixkqodc")
                conn.setRequestProperty("X-NCP-APIGW-API-KEY", "L8X3Z2MYkfx4nrhFglCnDYfwSIRCiQx0nAhCrcvx")
                conn.doInput = true

                val responseCode = conn.responseCode

                val bufferedReader: BufferedReader
                if (responseCode == 200) {
                    bufferedReader = BufferedReader(InputStreamReader(conn.inputStream))
                } else {
                    bufferedReader = BufferedReader(InputStreamReader(conn.errorStream))
                }

                var line: String?
                while (bufferedReader.readLine().also { line = it } != null) {
                    stringBuilder.append(line + "\n")
                }

                var indexFirst: Int
                var indexLast: Int

                indexFirst = stringBuilder.indexOf("\"x\":\"")
                indexLast = stringBuilder.indexOf("\",\"y\":")
                val x = stringBuilder.substring(indexFirst + 5, indexLast)

                indexFirst = stringBuilder.indexOf("\"y\":\"")
                indexLast = stringBuilder.indexOf("\",\"distance\":")
                val y = stringBuilder.substring(indexFirst + 5, indexLast)

                bufferedReader.close()
                conn.disconnect()

                return arrayOf<String>(x, y)
            } else {
                return emptyArray()
            }
        } catch(e : Exception)
        {
            e.printStackTrace()
            return emptyArray()
        }
    }

    // -------------------------------------------------

    // -------------------------------------------------
    // 마커 생성
    private fun setMarker(target: Array<String>) {
        val marker = Marker()
        val targetLocation = LatLng(target[1].toDouble(), target[0].toDouble())
        marker.position = targetLocation
        marker.map = naverMap

        val cameraUpdate = CameraUpdate.scrollTo(targetLocation)
        naverMap.moveCamera(cameraUpdate)
    }
    // -------------------------------------------------

    // -------------------------------------------------
    // 지도 초기 설정

    private fun initMapView() {
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
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
        naverMap.locationTrackingMode = LocationTrackingMode.Face
    }

    // -------------------------------------------------

}