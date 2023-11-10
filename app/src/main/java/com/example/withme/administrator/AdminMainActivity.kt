package com.example.withme.administrator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.withme.R
import com.example.withme.databinding.ActivityAdminMainBinding

class AdminMainActivity : AppCompatActivity() {
    private lateinit var adminHomeFragment: AdminHomeFragment
    private lateinit var adminDepositFragment: AdminDepositFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // home 메인 설정
        if (savedInstanceState == null) {
            val fragment = AdminHomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.admin_fragment_frame, fragment, fragment.javaClass.simpleName)
                .commit()
        }

        val bottomNav = binding.adminBottomNavigation // BottomNavigationView 찾기

        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.admin_navigation_home -> {
                    adminHomeFragment = AdminHomeFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.admin_fragment_frame, adminHomeFragment).commit()
                    true
                }

                R.id.admin_navigation_deposit -> {
                    adminDepositFragment = AdminDepositFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.admin_fragment_frame, adminDepositFragment).commit()
                    true
                }

                else -> false
            }
        }
    }
}