package com.example.withme

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.withme.administrator.AdminMainActivity
import com.example.withme.databinding.FragmentSettingBinding
import org.json.JSONException
import org.json.JSONObject

class SettingFragment : Fragment() {
    lateinit var pref: SharedPreferences
    lateinit var editor : Editor

    companion object {
        fun newInstance() : SettingFragment {
            return SettingFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pref = (activity as AppCompatActivity).getPreferences(Context.MODE_PRIVATE)
        editor = pref.edit()
        val binding = FragmentSettingBinding.inflate(layoutInflater)
        (activity as AppCompatActivity).setSupportActionBar(binding.settingToolbar)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val sharedPreference : SharedPreferences = (activity as AppCompatActivity).getSharedPreferences("other", 0)
        val userId = sharedPreference.getString("id", "")

        binding.faqButton.setOnClickListener {
            val intent = Intent(requireActivity(), FaqActivity::class.java)  // 로그아웃 후, 로그인 액티비티로 이동
            startActivity(intent)
        }

        binding.notificationToggle.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("NotificationEnable", isChecked)
        }

        binding.withdrawButton.setOnClickListener {
            val builder = AlertDialog.Builder(activity as AppCompatActivity)
            builder.setTitle("정말 탈퇴하시겠습니까?")
                .setMessage("회원님의 모든 계정 정보가 삭제됩니다.")
                .setPositiveButton("예",
                    DialogInterface.OnClickListener {
                            dialog, id ->

                        val params = JSONObject()
                        params.put("id", userId)

                        val request = JsonObjectRequest(
                            Request.Method.POST,
                            resources.getString(R.string.ip) + "/delete_account",
                            params,
                            Response.Listener { response ->
                                try {
                                    val intent = Intent(requireContext(), LoginActivity::class.java)
                                    startActivity(intent)
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            },
                            Response.ErrorListener { error ->
                                error.printStackTrace()
                                Toast.makeText(requireContext(), "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                            }
                        )

                        // Volley 요청을 큐에 추가
                        Volley.newRequestQueue(requireContext()).add(request)
                    })
                .setNegativeButton("아니요",
                    DialogInterface.OnClickListener {
                            dialog, id ->
                    })
            builder.show()
        }

        return binding.root
    }
}