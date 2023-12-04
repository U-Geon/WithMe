package com.example.withme.administrator

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.withme.R
import com.example.withme.databinding.ActivityAdminKidInformationBinding
import org.json.JSONException
import org.json.JSONObject

class AdminKidInformationActivity : DialogFragment() {

    private var binding: ActivityAdminKidInformationBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var binding = ActivityAdminKidInformationBinding.inflate(inflater, container, false)
        // dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        /**
         * {
         *      "kidName": xxx,
         *      "phoneNumber": xxx,
         *      "rrn": xxx,
         *      "status": xxx
         * }
         */

        // 아이 정보 받아오기
        val url = resources.getString(R.string.ip) + "/admin_kid_info"

        val phoneNumber = binding.phoneNumber
        val kidName = binding.kidName
        val rrn = binding.rrn
        val status = binding.status

        val stringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener { response ->
                // 응답 처리
                try {
                    val jsonObject = JSONObject(response)

                    // json value값 받아서 text로 넣기.
                    val result1 = jsonObject.getString("kidName")
                    val result2 = jsonObject.getString("phoneNumber")
                    val result3 = jsonObject.getString("rrn")
                    val result4 = jsonObject.getString("status")

                    kidName.text = "이름: $result1"
                    phoneNumber.text = result2
                    rrn.text = "주민등록번호: $result3"
                    status.text = result4


                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                // 에러 처리
            }
        ){}

        // 요청 대기열에 요청 추가
        Volley.newRequestQueue(requireContext()).add(stringRequest)


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}