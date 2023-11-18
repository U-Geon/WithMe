package com.akj.withme

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.akj.withme.databinding.ActivityServiceBinding
import android.widget.Toast
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.util.Log
import android.view.View
import android.webkit.WebViewClient
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.fragment.app.FragmentManager
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.util.MarkerIcons

class ServiceActivity : AppCompatActivity(), OnMapReadyCallback {
    private val LOCATION_PERMISSION_REQUEST_CODE = 5000
    private val PERMISSIONS = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private lateinit var bind: ActivityServiceBinding
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    private var startName: String? = null
    private var startAddress: String? = null
    private var hospitalAddress: String? = null
    private var endAddress: String? = null

    private val startColor: Int = Color.GREEN
    private val hospitalColor: Int = Color.YELLOW
    private val endColor: Int = Color.CYAN

    private var startMarker: Marker? = null
    private var hospitalMarker: Marker? = null
    private var endMarker: Marker? = null

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
            bind.checkView.visibility = View.VISIBLE
            bind.checkView.bringToFront()
            bind.slidingDrawer.close()

            if(startAddress == null || hospitalAddress == null || endAddress == null)
                Toast.makeText(this, "모든 장소를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            else {
                bind.checkNameText.text = startName
                bind.checkAddressText.text = startAddress
            }
        }

        bind.finalSubmitButton.setOnClickListener {
            val url = "http://10.0.2.2:9001/submit"
            val params = JSONObject()
            params.put("startAddress", startAddress)
            params.put("hospitalAddress", hospitalAddress)
            params.put("endAddress", endAddress)

            val request = JsonObjectRequest(
                Request.Method.POST,
                url,
                params,
                { response ->
                    Log.d("submittest", response.getString("OK"))
                    try {
                    Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
                    requestData()
                } catch(error: JSONException) {
                    error.printStackTrace()
                }
                },
                {
                        error -> {
                    error.printStackTrace()
                }
                })

            Volley.newRequestQueue(this).add(request)
        }

        val getResult: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val address: String? = result.data?.getStringExtra("address")
            val name: String? = result.data?.getStringExtra("name")
            val type: Int? = result.data?.getIntExtra("type", 0)
            if (type == 1) {
                bind.startInput.setText(name)
                startAddress = address
                startName = name
            } else if (type == 2) {
                bind.hospitalInput.setText(name)
                hospitalAddress = address
            } else {
                bind.endInput.setText(name)
                endAddress = address
            }
            if (address != null && type != null) {
                createMarker(address, type)
            }
        }

        bind.startInput.setOnClickListener {
            val intent = Intent(this, AddressSearchActivity::class.java)
            intent.putExtra("type", 1)
            getResult.launch(intent)
        }

        bind.startButton.setOnClickListener {
            val intent = Intent(this, AddressSearchActivity::class.java)
            intent.putExtra("type", 1)
            getResult.launch(intent)
        }

        bind.hospitalInput.setOnClickListener {
            val intent = Intent(this, AddressSearchActivity::class.java)
            intent.putExtra("type", 2)
            getResult.launch(intent)
        }

        bind.hospitalButton.setOnClickListener {
            val intent = Intent(this, AddressSearchActivity::class.java)
            intent.putExtra("type", 2)
            getResult.launch(intent)
        }

        bind.endInput.setOnClickListener {
            val intent = Intent(this, AddressSearchActivity::class.java)
            intent.putExtra("type", 3)
            getResult.launch(intent)
        }

        bind.endButton.setOnClickListener {
            val intent = Intent(this, AddressSearchActivity::class.java)
            intent.putExtra("type", 3)
            getResult.launch(intent)
        }

        bind.closeCheckButton.setOnClickListener {
            closeCheck()
        }
    }

    private fun requestData()
    {
        thread(start = true) {
            while (true) {
                val url = "http://10.0.2.2:9001/status"
                val params = JSONObject()
                val request = JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    params,
                    Response.Listener { response -> try {
                        val lat = response.getString("result")
                        Log.d("test", lat)
                    } catch(error: JSONException) {
                        error.printStackTrace()
                    }
                    },
                    {
                            error -> {
                        error.printStackTrace()
                    }
                    })

                Volley.newRequestQueue(this).add(request)
                Thread.sleep(2000)
            }
        }
    }

    private fun closeCheck()
    {
        bind.checkView.visibility = View.GONE
        bind.slidingDrawer.bringToFront()
        bind.slidingDrawer.open()
    }

    private fun createMarker(target: String, type: Int){
        var coordinates : Array<String> = emptyArray()
        val thr = thread(start = true) {
            coordinates = search(target)
            for(elem in coordinates)
                Log.d("TEST", elem)
        }

        thr.join()

        var markerColor : Int
        if(type == 1) markerColor = startColor
        else if(type == 2) markerColor = hospitalColor
        else markerColor = endColor

        if(!coordinates.isEmpty())
        {
            setMarker(coordinates, markerColor)
        }
        else
            Log.d("TEST", "비어있음")
    }

    // -------------------------------------------------
    // 주소 검색 (GeoCode)

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
    private fun setMarker(target: Array<String>, color: Int) {
        val marker = Marker()
        val targetLocation = LatLng(target[1].toDouble(), target[0].toDouble())
        marker.position = targetLocation
        marker.map = naverMap
        marker.icon = MarkerIcons.BLACK
        marker.iconTintColor = color

        var markerText: String
        if(color == startColor) {
            if(startMarker != null) {
                startMarker!!.map = null
            }
            startMarker = marker
            markerText = "출발"
        }
        else if(color == hospitalColor) {
            if(hospitalMarker != null) {
                hospitalMarker!!.map = null
            }
            hospitalMarker = marker
            markerText = "병원"
        }
        else {
            if(endMarker != null) {
                endMarker!!.map = null
            }
            endMarker = marker
            markerText = "도착"
        }

        marker.captionText = markerText
        marker.setCaptionAligns(Align.Top)

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
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
    }

    // -------------------------------------------------

}