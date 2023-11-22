package com.example.withme

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.withme.databinding.FragmentSettingBinding

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